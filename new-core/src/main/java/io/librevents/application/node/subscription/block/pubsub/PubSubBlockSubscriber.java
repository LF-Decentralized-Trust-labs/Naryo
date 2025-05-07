package io.librevents.application.node.subscription.block.pubsub;

import java.io.IOException;
import java.util.Map;

import io.librevents.application.common.Mapper;
import io.librevents.application.node.calculator.StartBlockCalculator;
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
            Mapper<Block, BlockEvent> blockMapper,
            StartBlockCalculator calculator) {
        super(interactor, dispatcher, node, blockMapper, calculator);
    }

    @Override
    public Disposable subscribe() throws IOException {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(
                interactor
                        .replayPastBlocks(calculator.getStartBlock())
                        .doOnComplete(() -> compositeDisposable.add(subscribeNewBlocks()))
                        .subscribe(onNext, onError));
        return compositeDisposable;
    }

    private Disposable subscribeNewBlocks() {
        return interactor.replyFutureBlocks().subscribe(onNext, onError);
    }
}
