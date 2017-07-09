package database.model;

import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import database.Database;
import database.QueryBuilder;
import database.RowMap;
import javafx.beans.property.SimpleIntegerProperty;
import database.Database.Callback;
import database.Database.Callback.OnErrorListener;

public abstract class Model<T extends Model> extends RecursiveTreeObject<T> {

    /**
     * Mapeia as colunas e seus respectivos valores da tupla do objeto
     * OBS: A primery key é mapeada automaticamente
     *
     * @return um Map que relaciona coluna e seu respectivo valor
     */
    abstract RowMap getValues();

    /**
     * @param rowMap O registro a ser transformado em objeto
     * @return a instância do objeto que contem as propriedades salvas no map
     */
    public abstract T from(RowMap rowMap);

    abstract String getWhereClauseForPrimaryKey();

    abstract boolean shouldUpdate();

    public String getTableName() {
        return getClass().getSimpleName().replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * Inicia o QueryBuilder com as colunas a serem selecionadas
     *
     * @param columns null para selecionar todas, ou as colunas específicas a serem selecionadas
     * @return o QueryBuilder para encadeamento de chamadas
     */
    public QueryBuilder<T> select(String... columns) {
        QueryBuilder<T> builder = new QueryBuilder<>();

        builder.setOnExecuteListener((query, callback) -> {
            Callback<RowMap> databaseCallback = new Callback<>();
            databaseCallback.onError(callback.getOnErrorListener());
            databaseCallback.onSuccess((rowMap) -> callback.onSuccess(from(rowMap)));

            Database.getInstance().select(query, databaseCallback);
        });

        return builder.select(columns).from(getTableName());
    }

    void onSave(Integer generatedKey) {

    }

    /**
     * Salva a model assincronamente
     *
     * @return um objeto de callback para notificação de sucesso/erro
     */
    public SaveCallback save() {
        SaveCallback saveCallback = new SaveCallback();

        Callback<Integer> callbackInternal = new Callback<>();
        callbackInternal.onSuccess((id) -> {
            onSave(id);
            saveCallback.onSuccess();
        }).onError(saveCallback::onError);

        RowMap values = getValues();

        if (shouldUpdate())
            Database.getInstance().update(getTableName(),
                    values,
                    getWhereClauseForPrimaryKey(),
                    callbackInternal, this instanceof CompositePrimaryKeyModel);
        else
            Database.getInstance().insert(getTableName(), values, callbackInternal, this instanceof CompositePrimaryKeyModel);

        return saveCallback;
    }

    /**
     * Remove a model do banco de dados
     */
    public void delete() {
        Database.getInstance().delete(getTableName(), getWhereClauseForPrimaryKey());
    }

    public static class SaveCallback {
        public interface OnSuccessListener {
            void onSuccess();
        }

        private OnSuccessListener mOnSuccessListener;
        private OnErrorListener mOnErrorListener;

        public SaveCallback onSuccess(OnSuccessListener onSuccessListener) {
            mOnSuccessListener = onSuccessListener;
            return this;
        }

        public SaveCallback onError(OnErrorListener onErrorListener) {
            mOnErrorListener = onErrorListener;
            return this;
        }

        void onSuccess() {
            if (mOnSuccessListener != null)
                mOnSuccessListener.onSuccess();
        }

        void onError(Exception exception) {
            if (mOnErrorListener != null)
                mOnErrorListener.onError(exception);
        }

    }
}
