package database;

import database.model.Model;
import util.Utils;

import java.util.ArrayList;
import java.util.List;

public class QueryBuilder<T extends Model> {

    public interface BuildNewQuery {
        void newQuery(QueryBuilder builder);
    }

    public interface OnExecuteListener<M extends Model> {
        void onExecute(String query, Database.Callback<M> callback);
    }

    private String[] mColumns = new String[]{};
    private String[] mFrom = new String[]{};
    private List<Where> mWheres = new ArrayList<>();
    private List<Join> mJoins = new ArrayList<>();
    private String[] mOrderBy = new String[]{};
    private String[] mGroupBy = new String[]{};
    private String mHaving = "";

    private OnExecuteListener<T> mOnExecuteListener;

    public QueryBuilder() {

    }

    private String buildQuery(BuildNewQuery buildNewQuery) {
        QueryBuilder queryBuilder = new QueryBuilder();
        buildNewQuery.newQuery(queryBuilder);
        return queryBuilder.toString();
    }

    public QueryBuilder<T> select(String... columns) {
        mColumns = columns;
        return this;
    }

    public QueryBuilder<T> from(BuildNewQuery fromBuilder) {
        return from(buildQuery(fromBuilder));
    }

    public QueryBuilder<T> from(String... from) {
        mFrom = from;
        return this;
    }

    private QueryBuilder<T> join(String type, String table, String leftOn, String comparator, String rightOn) {
        mJoins.add(new Join(type, table, leftOn, comparator, rightOn));
        return this;
    }

    private QueryBuilder<T> innerJoin(String table, String leftOn, String comparator, String rightOn) {
        return join("INNER JOIN", table, leftOn, comparator, rightOn);
    }

    private QueryBuilder<T> letJoin(String table, String leftOn, String comparator, String rightOn) {
        return join("LEFT JOIN", table, leftOn, comparator, rightOn);
    }

    private QueryBuilder<T> rightJoin(String table, String leftOn, String comparator, String rightOn) {
        return join("RIGHT JOIN", table, leftOn, comparator, rightOn);
    }

    private QueryBuilder<T> where(String logicalOperator, String column, String comparator, Object value) {
        if (value == null) {
            value = "null";
            if (comparator.equals("="))
                comparator = "is";
            else if (comparator.equals("<>"))
                comparator = "not";
        }

        mWheres.add(new Where(logicalOperator, column, comparator, value));
        return this;
    }

    public QueryBuilder<T> where(String column, String comparator, Object value) {
        return where("AND", column, comparator, value);
    }

    public QueryBuilder<T> orWhere(String column, String comparator, Object value) {
        return where("OR", column, comparator, value);
    }

    /**
     * WHERE NULL
     */
    private QueryBuilder<T> whereNullInternal(String logicalOperator, String column, boolean isNull) {
        return where(logicalOperator, column, isNull ? "is" : "not", "null");
    }

    public QueryBuilder<T> whereNull(BuildNewQuery whereBuilder) {
        return whereNullInternal("AND", "(" + buildQuery(whereBuilder) + ")", true);
    }

    public QueryBuilder<T> whereNull(String column) {
        return whereNullInternal("AND", column, true);
    }

    public QueryBuilder<T> orWhereNull(BuildNewQuery whereBuilder) {
        return whereNullInternal("OR", "(" + buildQuery(whereBuilder) + ")", true);
    }

    public QueryBuilder<T> orWhereNull(String column) {
        return whereNullInternal("OR", column, true);
    }

    /**
     * WHERE NOT NULL
     */
    public QueryBuilder<T> whereNotNull(BuildNewQuery whereBuilder) {
        return whereNullInternal("AND", "(" + buildQuery(whereBuilder) + ")", false);
    }

    public QueryBuilder<T> whereNotNull(String column) {
        return whereNullInternal("AND", column, false);
    }

    public QueryBuilder<T> orWhereNotNull(BuildNewQuery whereBuilder) {
        return whereNullInternal("OR", "(" + buildQuery(whereBuilder) + ")", false);
    }

    public QueryBuilder<T> orWhereNotNull(String column) {
        return whereNullInternal("OR", column, false);
    }

    /**
     * WHERE IN
     */
    private QueryBuilder<T> whereInInternal(String logicalOperator, String column, String value, boolean in) {
        return where(logicalOperator, column, in ? "in" : "not in", value);
    }

    public QueryBuilder<T> whereIn(String column, BuildNewQuery whereBuilder) {
        return whereInInternal("AND", column, "(" + buildQuery(whereBuilder) + ")", true);
    }

    public QueryBuilder<T> whereIn(String column, List<T> values) {
        return whereInInternal("AND", column, "(" + Utils.joinToSql(",", values) + ")", true);
    }

    public QueryBuilder<T> orWhereIn(String column, BuildNewQuery whereBuilder) {
        return whereInInternal("OR", column, "(" + buildQuery(whereBuilder) + ")", true);
    }

    public QueryBuilder<T> orWhereIn(String column, List<T> values) {
        return whereInInternal("OR", column, "(" + Utils.joinToSql(",", values) + ")", true);
    }

    /**
     * WHERE NOT IN
     */
    public QueryBuilder<T> whereNotIn(String column, BuildNewQuery whereBuilder) {
        return whereInInternal("AND", column, "(" + buildQuery(whereBuilder) + ")", false);
    }

    public QueryBuilder<T> whereNotIn(String column, List<T> values) {
        return whereInInternal("AND", column, "(" + Utils.joinToSql(",", values) + ")", false);
    }

    public QueryBuilder<T> orWhereNotIn(String column, BuildNewQuery whereBuilder) {
        return whereInInternal("OR", column, "(" + buildQuery(whereBuilder) + ")", false);
    }

    public QueryBuilder<T> orWhereNotIn(String column, List<T> values) {
        return whereInInternal("OR", column, "(" + Utils.joinToSql(",", values) + ")", false);
    }

    /**
     * ORDER BY
     */
    public QueryBuilder<T> orderBy(String... orderBy) {
        mOrderBy = orderBy;
        return this;
    }

    /**
     * GROUP BY
     */
    public QueryBuilder<T> groupBy(String... groupBy) {
        mGroupBy = groupBy;
        return this;
    }

    /**
     * HAVING
     */
    public QueryBuilder<T> having(Where where) {
        mHaving = where.toString();
        return this;
    }

    public QueryBuilder<T> having(BuildNewQuery whereBuilder) {
        mHaving = buildQuery(whereBuilder);
        return this;
    }

    /**
     * Constrói a cláusula SQL
     *
     * @return o query construído
     */
    @Override
    public String toString() {
        StringBuilder query = new StringBuilder();

        query.append("SELECT ")
                .append(mColumns == null ? "*" : Utils.join(",", mColumns))
                .append(" FROM ")
                .append(Utils.join(",", mFrom));

        for (Join join : mJoins)
            query.append(" ")
                    .append(join.toString())
                    .append(" ");

        for (Where where : mWheres)
            query.append(" ")
                    .append(where.toString())
                    .append(" ");

        query.append(Utils.join(",", mGroupBy))
                .append(" ")
                .append(mHaving)
                .append(" ")
                .append(Utils.join(",", mOrderBy));

        return query.toString();
    }

    public void setOnExecuteListener(OnExecuteListener<T> listener) {
        mOnExecuteListener = listener;
    }

    public void execute(Database.Callback<T> callback) {
        if (mOnExecuteListener != null)
            mOnExecuteListener.onExecute(toString(), callback);
    }

    public static class Where {
        private String mLogicalOperator;
        private String mColumn;
        private String mComparator;
        private Object mValue;

        public Where(String column, String comparator, Object value) {
            this(null, column, comparator, value);
        }

        Where(String logicalOperator, String column, String comparator, Object value) {
            mLogicalOperator = logicalOperator;
            mColumn = column;
            mComparator = comparator;
            mValue = value;
        }

        @Override
        public String toString() {
            StringBuilder where = new StringBuilder();

            if (mLogicalOperator != null)
                where.append(mLogicalOperator).append(" ");
            where.append(mColumn)
                    .append(" ")
                    .append(mComparator)
                    .append(" ");

            if (mValue instanceof String) {
                where.append("'")
                        .append(mValue)
                        .append("'");
            } else
                where.append(mValue);

            return where.toString();
        }
    }

    public static class Join {
        private String mType;
        private String mTable;
        private String mLeftOn;
        private String mComparator;
        private String mRightOn;

        Join(String type, String table, String leftOn, String comparator, String rightOn) {
            mType = type;
            mTable = table;
            mLeftOn = leftOn;
            mComparator = comparator;
            mRightOn = rightOn;
        }

        @Override
        public String toString() {
            String join = mType +
                    mTable +
                    " ON " +
                    mLeftOn +
                    mComparator +
                    " " +
                    mRightOn;

            return join;
        }
    }
}
