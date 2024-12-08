package model

import (
	"fmt"
	"strings"
)

type Models struct {
	ModelID       string
	ModelType     string
	ModelTypeName string
	ModelCos      string
}

func GetModelsIDAndCosByTypeAndName(modelType, modelName string) ([]string, []string, error) {
	var idres, cosres []string
	query := "SELECT modelID, modelcos FROM tbl_models WHERE modeltype = ? AND modeltypename = ?"
	rows, err := DB.Query(query, modelType, modelName)
	if err != nil {
		return nil, nil, err
	}
	for rows.Next() {
		var model Models
		err := rows.Scan(&model.ModelID, &model.ModelCos)
		if err != nil {
			return nil, nil, err
		}
		idres = append(idres, model.ModelID)
		cosres = append(cosres, model.ModelCos)
	}
	return idres, cosres, nil
}

func Insert(tableName string, data map[string]interface{}) error {
	// 构建插入语句，例如："INSERT INTO users (name, age) VALUES (?, ?)"
	var columns []string
	var values []interface{} // 直接使用interface{}类型的切片来存储值
	var placeholders []string
	for key, value := range data { // 同时遍历键和值
		columns = append(columns, key)
		placeholders = append(placeholders, "?")
		values = append(values, value) // 保持键和值的对应关系
	}
	query := fmt.Sprintf("INSERT INTO %s (%s) VALUES (%s)", tableName, strings.Join(columns, ", "), strings.Join(placeholders, ", "))

	// 执行插入语句
	stmt, err := DB.Prepare(query)
	if err != nil {
		return fmt.Errorf("prepare query failed: %w", err)
	}
	defer stmt.Close()

	// 执行插入操作
	_, err = stmt.Exec(values...)
	if err != nil {
		return fmt.Errorf("execute insert failed: %w", err)
	}

	return nil
}

func Search(tableName string, param map[string]interface{}) ([]map[string]interface{}, error) {
	// 构建查询条件
	var conditions []string
	var values []interface{}
	for key, value := range param {
		conditions = append(conditions, fmt.Sprintf("%s = ?", key))
		values = append(values, value)
	}

	// 构建查询语句
	query := "SELECT * FROM " + tableName
	if len(conditions) > 0 {
		query += " WHERE " + strings.Join(conditions, " AND ")
	}

	// 执行查询语句
	rows, err := DB.Query(query, values...)
	if err != nil {
		return nil, fmt.Errorf("query failed: %w", err)
	}
	defer rows.Close()

	// 获取列信息
	columns, err := rows.Columns()
	if err != nil {
		return nil, fmt.Errorf("get columns failed: %w", err)
	}

	// 创建一个切片来存储结果
	var result []map[string]interface{}
	for rows.Next() {
		// 创建一个map来存储当前行的数据
		columnValues := make([]interface{}, len(columns))
		columnPointers := make([]interface{}, len(columns))
		for i := range columnValues {
			columnPointers[i] = &columnValues[i]
		}

		// 扫描当前行的数据
		if err := rows.Scan(columnPointers...); err != nil {
			return nil, fmt.Errorf("scan failed: %w", err)
		}

		// 将列值转换为map
		entry := make(map[string]interface{})
		for i, col := range columns {
			var v interface{}
			val := columnPointers[i].(*interface{})
			if *val != nil {
				v = *val
			}
			entry[col] = v
		}
		result = append(result, entry)
	}

	if err := rows.Err(); err != nil {
		return nil, fmt.Errorf("rows error: %w", err)
	}

	return result, nil
}

func Update(tableName string, fields map[string]interface{}, condition map[string]interface{}) error {
	// 构造 SET 部分
	setClauses := []string{}
	values := []interface{}{}

	for field, value := range fields {
		setClauses = append(setClauses, fmt.Sprintf("%s = ?", field))
		values = append(values, value)
	}
	setSQL := strings.Join(setClauses, ", ")

	// 构造 WHERE 部分
	whereClauses := []string{}
	for field, value := range condition {
		whereClauses = append(whereClauses, fmt.Sprintf("%s = ?", field))
		values = append(values, value)
	}
	whereSQL := strings.Join(whereClauses, " AND ")

	// 构造完整的 SQL 更新语句
	sql := fmt.Sprintf("UPDATE %s SET %s WHERE %s", tableName, setSQL, whereSQL)

	// 执行 SQL 更新
	_, err := DB.Exec(sql, values...)
	if err != nil {
		return err
	}

	return nil
}
