## 构造函数

构造函数在合约部署时执行，它调用了 TableFactory 的 createTable 方法，创建了一个名为 t_test 的表格，其中 name 是主键，item_id 和 item_name 是值字段。TableFactory 的地址是固定的 0x1001。

## select 函数

select 函数用于查询表格中的数据，它接受一个字符串参数 name，表示要查询的主键值，它返回三个字符串数组，分别是 user_name_bytes_list, item_id_list 和 item_name_bytes_list，表示查询到的数据的字段值。如果没有查询到任何数据，返回的数组为空。

## insert 函数

insert 函数用于向表格中插入一条数据，它接受三个字符串参数 name, item_id 和 item_name，表示要插入的数据的字段值，它返回一个整数 count，表示插入操作影响的数据条数，它还触发了一个 InsertResult 事件，传递了 count 的值。

## update 函数

update 函数用于更新表格中的数据，它接受两个字符串参数 name 和 item_name，表示要更新的数据的主键值和新的值字段值，它返回一个整数 count，表示更新操作影响的数据条数，它还触发了一个 UpdateResult 事件，传递了 count 的值。

## remove 函数

remove 函数用于删除表格中的数据，它接受两个字符串参数 name 和 item_id，表示要删除的数据的主键值和值字段值，它返回一个整数 count，表示删除操作影响的数据条数，它还触发了一个 RemoveResult 事件，传递了 count 的值。