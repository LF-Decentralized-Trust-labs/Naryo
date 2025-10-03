package io.naryo.application.configuration.revision.manager;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import io.naryo.application.configuration.manager.CollectionConfigurationManager;
import io.naryo.application.configuration.revision.LiveView;
import io.naryo.application.configuration.revision.Revision;
import io.naryo.application.configuration.revision.RevisionConflictException;
import io.naryo.application.configuration.revision.diff.Diffs;
import io.naryo.application.configuration.revision.fingerprint.RevisionFingerprinter;
import io.naryo.application.configuration.revision.hook.RevisionHook;
import io.naryo.application.configuration.revision.operation.AddOperation;
import io.naryo.application.configuration.revision.operation.RemoveOperation;
import io.naryo.application.configuration.revision.operation.RevisionOperation;
import io.naryo.application.configuration.revision.operation.UpdateOperation;
import io.naryo.application.configuration.revision.registry.LiveRegistry;

public abstract class DefaultConfigurationRevisionManager<T>
        implements ConfigurationRevisionManager<T> {

    protected final AtomicLong revisionVersion = new AtomicLong(0);
    protected final List<RevisionHook<T>> hooks = new CopyOnWriteArrayList<>();
    protected final CollectionConfigurationManager<T> configurationManager;
    protected final RevisionFingerprinter<T> fingerprinter;
    protected final Function<T, UUID> idFn;
    protected final LiveRegistry<T> live;
    protected volatile LiveView<T> view;

    protected DefaultConfigurationRevisionManager(
            CollectionConfigurationManager<T> configurationManager,
            RevisionFingerprinter<T> fingerprinter,
            Function<T, UUID> idFn,
            LiveRegistry<T> live) {
        this.configurationManager = configurationManager;
        this.fingerprinter = fingerprinter;
        this.live = live;
        this.idFn = idFn;
    }

    @Override
    public Revision<T> initialize() {
        Collection<T> conf = configurationManager.load();
        if (conf == null) conf = List.of();
        String hash = fingerprinter.revisionHash(conf, idFn);

        var revision = new Revision<>(revisionVersion.get(), hash, conf);
        view =
                new LiveView<>(
                        revision,
                        conf.stream().collect(Collectors.toMap(idFn, t -> t)),
                        conf.stream().collect(Collectors.toMap(idFn, fingerprinter::itemHash)));

        live.refresh(revision);
        return revision;
    }

    @Override
    public Revision<T> apply(RevisionOperation<T> op) {
        var curView = view;
        var current = curView == null ? List.<T>of() : curView.revision().domainItems();
        var byId = curView == null ? Map.<UUID, T>of() : curView.byId();
        var itemH = curView == null ? Map.<UUID, String>of() : curView.itemFingerprintById();

        Collection<T> newDomain =
                switch (op.kind()) {
                    case ADD -> applyAdd(current, byId, itemH, (AddOperation<T>) op);
                    case UPDATE -> applyUpdate(current, byId, itemH, (UpdateOperation<T>) op);
                    case REMOVE -> applyRemove(current, byId, itemH, (RemoveOperation<T>) op);
                };

        var newHash = fingerprinter.revisionHash(newDomain, idFn);
        if (curView != null && curView.revision().hash().equals(newHash)) {
            return curView.revision();
        }

        Map<UUID, String> newItemH =
                newDomain.stream().collect(Collectors.toMap(idFn, fingerprinter::itemHash));
        var diff =
                Diffs.diff(
                        current,
                        newDomain,
                        idFn,
                        (a, b) ->
                                Objects.equals(
                                        itemH.get(idFn.apply(a)), newItemH.get(idFn.apply(b))));

        for (var h : hooks) {
            try {
                h.onBeforeApply(diff);
            } catch (Exception e) {
                throw new RuntimeException("onBeforeApply hook error", e);
            }
        }

        var applied = publish(newDomain);

        for (var h : hooks) {
            try {
                h.onAfterApply(applied, diff);
            } catch (Exception e) {
                throw new RuntimeException("onAfterApply hook error", e);
            }
        }

        return applied;
    }

    @Override
    public void registerHook(RevisionHook<T> hook) {
        if (hook == null) return;
        hooks.add(hook);
    }

    @Override
    public LiveView<T> liveView() {
        return view;
    }

    private Revision<T> publish(Collection<T> canonical) {
        var hash = fingerprinter.revisionHash(canonical, idFn);
        var rev = new Revision<>(revisionVersion.incrementAndGet(), hash, List.copyOf(canonical));
        int cap = Math.max(16, (int) (canonical.size() / 0.75f) + 1);
        Map<UUID, T> byId = new HashMap<>(cap);
        Map<UUID, String> itemH = new HashMap<>(cap);
        for (T t : canonical) {
            UUID id = idFn.apply(t);
            byId.put(id, t);
            itemH.put(id, fingerprinter.itemHash(t));
        }

        this.view = new LiveView<>(rev, byId, itemH);
        live.refresh(rev);
        return rev;
    }

    private Collection<T> applyAdd(
            Collection<T> current, Map<UUID, T> byId, Map<UUID, String> itemH, AddOperation<T> op) {
        var id = idFn.apply(op.item());
        var cur = byId.get(id);
        if (cur == null) {
            var out = new ArrayList<>(current);
            out.add(op.item());
            return List.copyOf(out);
        }

        if (Objects.equals(itemH.get(id), fingerprinter.itemHash(op.item()))) return current;
        throw new RevisionConflictException(
                id,
                op.kind(),
                "Item exists with different contents (expectedHash="
                        + itemH.get(id)
                        + ", newHash="
                        + fingerprinter.itemHash(op.item())
                        + ")");
    }

    private Collection<T> applyUpdate(
            Collection<T> current,
            Map<UUID, T> byId,
            Map<UUID, String> itemH,
            UpdateOperation<T> op) {
        var cur = byId.get(op.id());
        if (cur == null) throw new RevisionConflictException(op.id(), op.kind(), "Item not found");
        var actualHash = itemH.get(op.id());
        if (!Objects.equals(actualHash, op.prevItemHash()))
            throw new RevisionConflictException(
                    op.id(),
                    op.kind(),
                    "Item changed since base (expectedPrevHash="
                            + op.prevItemHash()
                            + ", actualPrevHash="
                            + actualHash
                            + ")");
        var out = new ArrayList<>(current);
        for (int i = 0; i < out.size(); i++) {
            if (Objects.equals(idFn.apply(out.get(i)), op.id())) {
                out.set(i, op.proposed());
                return List.copyOf(out);
            }
        }
        throw new RevisionConflictException(op.id(), op.kind(), "Item not found during update");
    }

    private Collection<T> applyRemove(
            Collection<T> current,
            Map<UUID, T> byId,
            Map<UUID, String> itemH,
            RemoveOperation<T> op) {
        var cur = byId.get(op.id());
        if (cur == null) return current;
        var actualHash = itemH.get(op.id());
        if (!Objects.equals(actualHash, op.prevItemHash()))
            throw new RevisionConflictException(
                    op.id(),
                    op.kind(),
                    "Item changed since base (expectedPrevHash="
                            + op.prevItemHash()
                            + ", actualPrevHash="
                            + actualHash
                            + ")");
        return current.stream().filter(t -> !Objects.equals(idFn.apply(t), op.id())).toList();
    }
}
