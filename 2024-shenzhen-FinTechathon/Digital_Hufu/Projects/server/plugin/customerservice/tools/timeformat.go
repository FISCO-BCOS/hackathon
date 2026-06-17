package tools

import "time"

func FormatTimestamp(timestamp int64) string {
	t := time.Unix(timestamp, 0)
	now := time.Now()

	// 格式化时间
	if t.Year() == now.Year() && t.YearDay() == now.YearDay() {
		// 当天，返回 24 小时制的时和分
		return t.Format("15:04")
	} else if t.Year() == now.Year() && t.YearDay() == now.YearDay()-1 {
		// 昨天，返回 "昨天"
		return "昨天"
	} else {
		// 其他时间，返回月和日
		return t.Format("01-02")
	}
}
