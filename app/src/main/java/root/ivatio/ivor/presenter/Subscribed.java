package root.ivatio.ivor.presenter;

/**
 * Обозначает любые компоненты, связанные с Rx.
 * Данный интерфейс показывает, что у компонента есть подписки и он должен вовремя от них отписаться
 */
public interface Subscribed {
    void unsubscribe();
}
