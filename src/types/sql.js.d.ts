declare module 'sql.js' {
  interface SqlJsStatic {
    Database: new (data?: ArrayLike<number> | Buffer | null) => Database;
  }

  interface QueryExecResult {
    columns: string[];
    values: unknown[][];
  }

  interface Statement {
    bind(params?: unknown[]): boolean;
    step(): boolean;
    getAsObject(): Record<string, unknown>;
    free(): void;
    reset(): void;
  }

  interface Database {
    run(sql: string, params?: unknown[]): Database;
    exec(sql: string): QueryExecResult[];
    prepare(sql: string): Statement;
    export(): Uint8Array;
    close(): void;
  }

  interface InitSqlJsOptions {
    locateFile?: (file: string) => string;
    wasmBinary?: ArrayLike<number> | Buffer;
  }

  function initSqlJs(options?: InitSqlJsOptions): Promise<SqlJsStatic>;

  export default initSqlJs;
  export { Database, Statement, QueryExecResult, SqlJsStatic, InitSqlJsOptions };
}
