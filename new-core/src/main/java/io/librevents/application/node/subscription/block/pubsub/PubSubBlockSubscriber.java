package io.librevents.application.node.subscription.block.pubsub;

import java.util.Map;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.dispatch.Dispatcher;
import io.librevents.application.node.interactor.block.BlockInteractor;
import io.librevents.application.node.interactor.block.dto.Block;
import io.librevents.application.node.subscription.block.BlockSubscriber;
import io.librevents.domain.event.block.BlockEvent;
import io.librevents.domain.node.Node;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class PubSubBlockSubscriber extends BlockSubscriber {

    private final Consumer<Block> onNext =
            block -> {
                log.info("Processing block {}", block.number());
                dispatcher.dispatch(blockMapper.map(block, Map.of("nodeId", node.getId())));
            };

    private final Consumer<Throwable> onError =
            throwable -> {
                log.error("Error processing block", throwable);
                // TODO: Handle error
            };

    public PubSubBlockSubscriber(
            BlockInteractor interactor,
            Dispatcher dispatcher,
            Node node,
            Mapper<Block, BlockEvent> blockMapper) {
        super(interactor, dispatcher, node, blockMapper);
    }

    @Override
    public Disposable subscribe() {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                interactor
                        .replayPastBlocks(getStartBlock())
                        .doOnComplete(() -> compositeDisposable.add(subscribeNewBlocks()))
                        .subscribe(onNext, onError));
        return compositeDisposable;
    }

    private Disposable subscribeNewBlocks() {
        return interactor.replyFutureBlocks().subscribe(onNext, onError);
    }
}
