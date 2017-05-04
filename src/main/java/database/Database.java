package database;

import database.model.Model;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Database {

    private static final String HOST = "localhost";
    private static final String PORT = "5432";
    private static final String DATABASE_NAME = "app";
    private static final String URL = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DATABASE_NAME;
    private static final String USER = "postgres";
    private static final String PASSWORD = "root";

    private ExecutorService mDatabaseExecutor;

    private static Database mInstance;

    private Database() {
        mDatabaseExecutor = Executors.newCachedThreadPool();
    }

    /**
     * Singleton
     *
     * @return a instância para o objeto do banco de dados
     */
    public static Database getInstance() {
        if (mInstance == null)
            mInstance = new Database();
        return mInstance;
    }

    /**
     * Fecha um ResultSet, ignorando a exception relacionada
     *
     * @param resultSet o ResultSet a ser fechado
     */
    private void closeResultSet(ResultSet resultSet) {
        try {
            resultSet.close();
        } catch (SQLException e) {
            //
        }
    }

    /**
     * Constrói uma query de insert/update
     *
     * @param tableName        Nome da tabela relacionada ao insert/update
     * @param columnValuePairs os pares coluna => valor da coluna do registro
     * @param where            cláusula where (opcional) do update
     * @return a cláusula SQL do insert/update
     */
    private String getSaveQuery(String tableName, Map<String, Object> columnValuePairs, QueryBuilder.Where where) {
        StringBuilder saveSQL = new StringBuilder();

        boolean inserting = where == null;

        saveSQL.append((inserting ? "INSERT INTO " : "UPDATE "))
                .append(tableName);

        if (inserting) {
            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, Object> pair : columnValuePairs.entrySet()) {
                if (first)
                    first = false;
                else {
                    columns.append(",");
                    values.append(",");
                }
                columns.append(pair.getKey());
                values.append(pair.getValue());
            }

            saveSQL.append(" (")
                    .append(columns)
                    .append(") VALUES (")
                    .append(values)
                    .append(")");
        } else {
            saveSQL.append(" SET ");
            int i = 0;
            for (Map.Entry<String, Object> pair : columnValuePairs.entrySet()) {
                saveSQL.append(pair.getKey())
                        .append("=")
                        .append(pair.getValue());
                if (i != columnValuePairs.size() - 1)
                    saveSQL.append(",");
                i++;
            }

            saveSQL.append(" WHERE ").append(where.toString());
        }

        return saveSQL.toString();
    }

    /**
     * Atalho para o método save; atualiza o registro no banco de dados
     */
    public void update(String tableName, Map<String, Object> columnValuePairs, QueryBuilder.Where where, Callback<Integer> callback) {
        save(tableName, columnValuePairs, where, callback);
    }

    /**
     * Atalho para o método save; insere o registro no banco de dados
     */
    public void insert(String tableName, Map<String, Object> columnValuePairs, Callback<Integer> callback) {
        save(tableName, columnValuePairs, null, callback);
    }

    /**
     * Insere ou atualiza um registro no banco de dados
     *
     * @param tableName        Mome da tabela que receberá o registro
     * @param columnValuePairs Conjunto coluna => valor da coluna do registro
     * @param where            (Opcional) where a ser utilizado para um update
     * @param callback         Callback para o retorno da execução assíncrona, sendo o ID do registro em caso de sucesso
     */
    private void save(String tableName, Map<String, Object> columnValuePairs, QueryBuilder.Where where, Callback<Integer> callback) {
        mDatabaseExecutor.execute(() -> {
            ResultSet resultSet = null;

            try (Connection connection = getConnection(); Statement statement = connection.createStatement()) {
                statement.execute(getSaveQuery(tableName, columnValuePairs, where));

                resultSet = statement.getGeneratedKeys();
                if (resultSet.next())
                    callback.onSuccess(resultSet.getInt(1));
                else
                    callback.onError();
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
                callback.onError();
            } finally {
                if (resultSet != null)
                    closeResultSet(resultSet);
            }
        });
    }

    /**
     * Método que retorna uma model a ser utilizada para select
     * (usado simplesmente para melhor legibilidade)
     *
     * @param modelClass a classe relacionada ao acesso ao banco de dados
     * @param <T>        model
     * @return
     */
    public static <T extends Model> T from(Class<T> modelClass) {
        try {
            return modelClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new InvalidClassException("A classe " + modelClass.getClass().getName() + " é inválida. Cheque a existência de um construtor sem parâmetros.");
        }
    }

    /**
     * Executa a cláusula SQL, invocando o callback para cada registro no resultSet
     *
     * @param SQL      o query a ser executado
     * @param callback callback de notificação para cada registro encontrado ou de exceptions ocorridas
     */
    public void select(String SQL, Callback<RowMap> callback) {
        mDatabaseExecutor.execute(() -> {
            try (Connection connection = getConnection(); Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(SQL)) {

                //Colunas
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                String columns[] = new String[columnCount];
                for (int i = 0; i < columns.length; i++)
                    columns[i] = resultSetMetaData.getColumnName(i + 1);

                while (resultSet.next()) {
                    RowMap row = new RowMap();
                    int i = 1;
                    for (String column : columns) {
                        row.put(column, resultSet.getObject(i));
                        i++;
                    }
                    callback.onSuccess(row);
                }
            } catch (ClassNotFoundException | SQLException e) {
                callback.onError();
            }
        });
    }

    /**
     * Busca o driver do postgres e realiza a tentativa de conexão
     *
     * @return a conexão para o banco de dados postgres
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(
                URL, USER, PASSWORD);
    }

    static class InvalidClassException extends RuntimeException {
        InvalidClassException(String message) {
            super(message);
        }
    }

    public static class Callback<R> {
        public interface OnSuccessListener<R> {
            void onSuccess(R response);
        }

        public interface OnErrorListener {
            void onError();
        }

        private OnSuccessListener<R> mOnSuccessListener;
        private OnErrorListener mOnErrorListener;

        public Callback onSuccess(OnSuccessListener<R> onSuccessListener) {
            mOnSuccessListener = onSuccessListener;
            return this;
        }

        public Callback onError(OnErrorListener onErrorListener) {
            mOnErrorListener = onErrorListener;
            return this;
        }

        public void onSuccess(R response) {
            if (mOnSuccessListener != null)
                mOnSuccessListener.onSuccess(response);
        }

        public void onError() {
            if (mOnErrorListener != null)
                mOnErrorListener.onError();
        }

        public OnErrorListener getOnErrorListener() {
            return mOnErrorListener;
        }
    }
}
