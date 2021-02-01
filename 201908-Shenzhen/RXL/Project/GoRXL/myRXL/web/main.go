package main

import(
	"html/template"
	"io"
	"log"
	"github.com/labstack/echo"
	"net/http"
	"strconv"
	"time"
	"client"
	"fmt"
	"rsa"
	"encoding/hex"
)

//渲染
type Template struct {
	templates *template.Template
}
  
func (t *Template) Render(w io.Writer, name string, data1 interface{}, c echo.Context) error {
	return t.templates.ExecuteTemplate(w, name, data1)
}

//基础页面
func index(c echo.Context) error{
	log.Println("Invoke Succeeded")
	return c.File("index.html")
}

func firm(c echo.Context) error{
	log.Println("Invoke Succeeded")
	return c.File("black_firm.html")
}

func school(c echo.Context) error{
	log.Println("Invoke Succeeded")
	return c.File( "black_school.html")
}

func person(c echo.Context) error{
	log.Println("Invoke Succeeded")
	return c.File("black_person.html")
}

func firm_act_info(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File("initial_activity_info.html")
}

func firm_act_update(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File("update_activity_124.html")
}

func firm_act_update_stu(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File("inquire_stu_info2.html")
}

func firm_query(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File("inquire_stu_info.html")
}

func school_query(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File("inquire_stu_info.html")
}

func school_act_info(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File( "initial_activity_info.html")
}

func school_act_update(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File("update_activity.html")
}

func school_act_update_stu(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File( "update_activity.html")
}

func school_cert_info(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File( "initial_certification_info.html")
}

func school_cert_update(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File("update_certification.html")
}

func school_grade_update_stu(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File( "update_stu_grade.html")
}

func school_stu_update(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File( "update_stu_info.html")
}

func school_stu_info(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File( "initial_stu_info.html")
}

func persion_query(c echo.Context) error {
	log.Println("Invoke Succeeded")
	return c.File("black_person.html")
}
//学生信息更新
func person_update(c echo.Context)error{
	temp1 := c.FormValue("certID")
	temp2, err := strconv.ParseInt(temp1, 10, 64)
	if err != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err,})
	}
	CertID := uint64(temp2)
	u, err1:= client.StuCertQuery(CertID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	log.Println("grpc: person_update : success")
	return c.Render(http.StatusOK, "update_stu_grade", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"major": u.Major,
	})
}
//个人信息初始化
func person_info(c echo.Context)error{
	return c.File("update_stu_info.html")
}
//个人信息更新
func person_update_before(c echo.Context)error{
	return c.File("update_stu_info.html")
}
//查询学生信息
func stu_info(c echo.Context) error{
	return c.File("inquire_stu_info1.html")
}

//活动信息初始化
func activity_info_input(c echo.Context) error{
	var u= client.ActivityInfo{}
	temp1 := c.FormValue("actID")
	
	temp2, err2 := strconv.ParseInt(temp1, 10, 64)
	u.ActID = uint32(temp2)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	u.ActName = c.FormValue("actName")
	u.Organizer = c.FormValue("sponsor")
	u.Status = c.FormValue("status") 
	u.ExtInfo = c.FormValue("extInfo")
	u.Time = uint64(time.Now().Unix())
	log.Println("Invoke Succeeded")
	temp3, err3:= client.ActivityRegister(u)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err3,})
	}
	fmt.Println("grpc:activity_info_input ", temp3)
	t := time.Now().Format("2006-01-02")
	return c.Render(http.StatusOK, "show_activity", map[string]interface{}{"actID": u.ActID, "actName": u.ActName,"status": u.Status, "extInfo": u.ExtInfo,"time":t,"sponsor":u.Organizer,})
}
//公司活动信息更新
func activity_info_update(c echo.Context) error {
	actID := c.FormValue("actID")
	actName := c.FormValue("actName")
	log.Println("Invoke Succeeded")
	log.Println("actID: ", actID)
	log.Println("actName: ", actName)
	return c.File("update_activity_123.html")
}
//学校活动信息更新
func school_act(c echo.Context) error {
	return c.File("update_activity_124.html")
}
//学校活动更新写入链
func write_update_activity(c echo.Context)error{
	var u= client.ActivityInfo{}
	
	temp1 := c.FormValue("actID")
	temp2, err1 := strconv.ParseInt(temp1, 10, 64)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	u.ActID = uint32(temp2)
	u.ActName = c.FormValue("actname")
	u.Organizer = c.FormValue("sponsor")
	u.Status = c.FormValue("status")
	u.ExtInfo = c.FormValue("extInfo")
	u.Time = uint64(time.Now().Unix())
	err2 := client.ActivityInfoUpdate(u)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	log.Println("grpc:write_update_activity success")
	return c.File("black_school.html")
}

//活动查询
func act_info_inquery(c echo.Context) error{
	actID := c.FormValue("actID")
	actName := c.FormValue("actName")
	log.Println("Invoke Succeeded")
	log.Println("actID: ", actID)
	log.Println("actName: ", actName)
	return c.File("update_stu_activity.html")
}

func show_stu_activity(c echo.Context) error{
	return c.File("show_stu_activity.html")
}
//活动成绩录入页面
func before_actgrade(c echo.Context) error{
	return c.File("before_actgrade.html")
}
//活动成绩录入链
func actgrade(c echo.Context) error{
	var u= client.ActivityRecode{}
	temp1 := c.FormValue("actID")
	temp2, err1 := strconv.ParseInt(temp1, 10, 64)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	u.ActID = uint32(temp2)
	temp3 := c.FormValue("stuID")
	temp4, err3 := strconv.ParseInt(temp3, 10, 64)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err3,})
	}
	u.StuID = uint64(temp4)
	u.ActName = c.FormValue("actName")
	u.StuName = c.FormValue("stuName")
	u.ExtInfo = c.FormValue("extInfo")
	u.Time = uint64(time.Now().Unix())
	u.ActSignature = c.FormValue("actSignature")
	t,err2 := client.ActivityGradeRecode(u)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	log.Println("grpc:Actgrade ",t)
	return c.File("black_school.html")
}
//活动成绩查询输入页面
func query_actgrade(c echo.Context)error{
	return c.File("query_actgrade.html")
}
//活动成绩显示
func query_actgrade_result(c echo.Context)error{
	temp1 := c.FormValue("actID")
	temp2, err1 := strconv.ParseInt(temp1, 10, 64)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	ActID := uint32(temp2)
	temp3 := c.FormValue("stuID")
	temp4, err3 := strconv.ParseInt(temp3, 10, 64)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err3,})
	}
	StuID := uint64(temp4)
	u, err2:= client.StuActQuery(StuID,ActID)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	t := time.Unix(int64(u.Time), 0).Format("2006-01-02 03:04:05 PM")
	log.Println("grpc: StuActQuery: success")
	return c.Render(http.StatusOK, "query_actgrade_result", map[string]interface{}{
		"actID": u.ActID,"stuID": u.StuID, "actName": u.ActName,"stuName":u.StuName,
		"extInfo":u.ExtInfo,"time":t,"actSignature":u.ActSignature,
	})
}
//证书发布
func cert_info(c echo.Context)error {
	temp1 := c.FormValue("stuID")
	temp2, err := strconv.ParseInt(temp1, 10, 64)
	if err != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err,})
	}
	stuID := uint64(temp2)
	u, err1:= client.StuInfoQuery(stuID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	return c.Render(http.StatusOK, "initial_certification_info", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"major": u.Major,
	})

}
//证书发布之前
func before_cert_info(c echo.Context)error{
	return c.File("initial_certification_info1.html")
}
//写入证书到链上
func write_cert_info(c echo.Context)error{
	var u= client.Certification{}
	
	temp1 := c.FormValue("stuID")
	temp2, err := strconv.ParseInt(temp1, 10, 64)
	if err != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err,})
	}
	u.StuID = uint64(temp2)
	temp3 := c.FormValue("certID")
	temp4, err2 := strconv.ParseInt(temp3, 10, 64)
	u.CertID = uint64(temp4)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	u.StuName = c.FormValue("stuName")
	u.UsName = c.FormValue("usName")
	u.Major = c.FormValue("major") 
	u.StudyTime = c.FormValue("studyTime")
	u.CertStatus = c.FormValue("certStatus")

	u.Time = uint64(time.Now().Unix())
	log.Println("Invoke Succeeded")
	str := temp1+temp3+u.StuName+u.UsName+u.Major+u.StudyTime+u.CertStatus
	private,public,err4 := rsa.GeneratePrivateKey(512)
	if err4 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err4,})
	}
	s := rsa.Signname(private,[]byte(str))
	log.Println("字符串：",str,"签名信息：",s)
	u.CertSignature = hex.EncodeToString(s)
	log.Println("签名后字符串：",u.CertSignature)
	u.ExtInfo = public
	u.Time = uint64(time.Now().Unix())
	temp5, err5:= client.CertInfoInit(u)
	if err5 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err5,})
	}
	fmt.Println("grpc: write_certification_update ", temp5)
	return c.Render(http.StatusOK, "after_certification_info", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"studyTime":u.StudyTime,"certStatus":u.CertStatus,"major": u.Major,"time":time.Now().Format("2006-01-02"),
		"public": public,
	})
}
//证书更新-查询信息
func cert_update(c echo.Context)error{
	return c.File("update_certification.html")
}
//证书更新
func cert_update_data(c echo.Context)error{
	temp1 := c.FormValue("certID")
	temp2, err := strconv.ParseInt(temp1, 10, 64)
	if err != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err,})
	}
	certID := uint64(temp2)
	u, err1:= client.StuCertQuery(certID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	log.Println("grpc: cert_update_data success")
	return c.Render(http.StatusOK, "update_certification_123", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"major": u.Major,
	})
}
//成绩更新前的查询
func grade_info1(c echo.Context)error{
	return c.File("update_stu_grade1.html")
}
//成绩更新
func grade_info(c echo.Context)error{
	temp1 := c.FormValue("stuID")
	temp2, err := strconv.ParseInt(temp1, 10, 64)
	if err != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err,})
	}
	stuID := uint64(temp2)
	u, err1:= client.StuInfoQuery(stuID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	log.Println("grpc: grade_info_update : success")
	return c.Render(http.StatusOK, "update_stu_grade", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"major": u.Major,
	})
	
}

//写入成绩到链上
func write_grade(c echo.Context)error{
	var u= client.GradeInfo{}
	
	temp1 := c.FormValue("stuID")
	temp2, err2 := strconv.ParseInt(temp1, 10, 64)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	u.StuID = uint64(temp2)	
	u.StuName = c.FormValue("stuName")

	temp3 := c.FormValue("grade")
	temp4, err := strconv.ParseInt(temp3, 10, 64)
	if err != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err,})
	}
	u.Grade = uint32(temp4)	
	log.Println("Grade :",u.Grade)
	temp5 := c.FormValue("averageGrades")
	temp6, err3 := strconv.ParseInt(temp5, 10, 64)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err3,})
	}
	u.AverageGrades = uint32(temp6)

	temp7 := c.FormValue("obligatoryCredit")
	temp8, err5 := strconv.ParseInt(temp7, 10, 64)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err5,})
	}
	u.ObligatoryCredit = uint32(temp8)

	temp9 := c.FormValue("optionalCredit")
	temp10, err6 := strconv.ParseInt(temp9, 10, 64)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err6,})
	}
	u.OptionalCredit = uint32(temp10)

	u.ExtInfo = c.FormValue("extInfo")	
	u.Time = uint64(time.Now().Unix())
	temp7, err4:= client.StuGradeUpdate(u)
	if err4 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err4,})
	}
	log.Println("grpc: write_grade ",temp7)
	
	return c.File("black_school.html")
}

//更新证书到链上
func write_certification_update(c echo.Context)error{
	var u= client.Certification{}
	
	temp1 := c.FormValue("stuID")
	temp2, err2 := strconv.ParseInt(temp1, 10, 64)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	u.StuID = uint64(temp2)
	temp3 := c.FormValue("certID")
	temp4, err3 := strconv.ParseInt(temp3, 10, 64)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err3,})
	}
	u.CertID = uint64(temp4)
	
	u.StuName = c.FormValue("stuName")
	u.UsName = c.FormValue("usName")
	u.Major = c.FormValue("major") 
	u.StudyTime = c.FormValue("studyTime")
	u.CertStatus = c.FormValue("certStatus")
	
	u.Time = uint64(time.Now().Unix())
	log.Println("Invoke Succeeded")
	str := temp1+temp3+u.StuName+u.UsName+u.Major+u.StudyTime+u.CertStatus
	private,public,err4 := rsa.GeneratePrivateKey(512)
	if err4 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err4,})
	}
	s := rsa.Signname(private,[]byte(str))
	u.CertSignature = hex.EncodeToString(s)
	u.ExtInfo = public

	u.Time = uint64(time.Now().Unix())
	temp5, err5:= client.CertInfoInit(u)
	if err5 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err5,})
	}
	fmt.Println("grpc: write_certification_update ", temp5)
	return c.Render(http.StatusOK, "after_certification_info", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"studyTime":u.StudyTime,"certStatus":u.CertStatus,"major": u.Major,"time":time.Now().Format("2006-01-02"),
		"public": public,
	})
}
//学生基本信息更新页面
func info_update(c echo.Context)error{

	return c.File("update_stu_info_123.html")
}
//写入学生信息到链上
func write_stu_info(c echo.Context)error{
	var u= client.StuInfo{}
	
	temp1 := c.FormValue("stuID")
	temp2, err2 := strconv.ParseInt(temp1, 10, 64)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	u.StuID = uint64(temp2)
	u.StuName = c.FormValue("stuName")
	u.UsName = c.FormValue("usName")
	u.Major = c.FormValue("major") 
	u.ExtInfo = c.FormValue("extInfo")

	temp3 := c.FormValue("usLevel")
	temp4, err3 := strconv.ParseInt(temp3, 10, 64)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err3,})
	}
	u.UsLevel = uint32(temp4)

	temp5 := c.FormValue("grade")
	temp6, err4 := strconv.ParseInt(temp5, 10, 64)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err4,})
	}
	
	u.Grade = uint32(temp6)
	log.Println("Grade: ",u.Grade)
	u.Time = uint64(time.Now().Unix())
	log.Println("Invoke Succeeded")
	temp3, err5:= client.StuInfoUpdate(u)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err5,})
	}
	fmt.Println("grpc: write_stu_info ", temp3)
	return c.File("black_school.html")
}
//跳转到学生信息初始化页面
func initial_stu_info(c echo.Context)error{

	return c.File("initial_stu_info.html")
}
//初始化学生信息入链
func write_stu_init(c echo.Context)error{
	var u= client.StuInfo{}
	
	temp1 := c.FormValue("stuID")
	temp2, err2 := strconv.ParseInt(temp1, 10, 64)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	u.StuID = uint64(temp2)
	u.StuName = c.FormValue("stuName")
	u.UsName = c.FormValue("usName")
	u.Major = c.FormValue("major") 
	u.ExtInfo = c.FormValue("extInfo")

	temp3 := c.FormValue("usLevel")
	temp4, err3 := strconv.ParseInt(temp3, 10, 64)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err3,})
	}
	u.UsLevel = uint32(temp4)

	temp5 := c.FormValue("grade")
	temp6, err4 := strconv.ParseInt(temp5, 10, 64)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err4,})
	}
	
	u.Grade = uint32(temp6)
	log.Println("Grade: ",u.Grade)
	u.Time = uint64(time.Now().Unix())
	log.Println("Invoke Succeeded")
	temp3, err5:= client.StuInfoInit(u)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err5,})
	}
	fmt.Println("grpc: write_stu_init ", temp3)
	return c.File("black_school.html")
}
//四类查询
func Inquery_info(c echo.Context)error{
	return c.File("Inquery_info.html")
}
func Inquery_activity(c echo.Context)error{

	return c.File("Inquery_activity.html")
}
func Inquery_grade(c echo.Context)error{

	return c.File("Inquery_grade.html")
}
func Inquery_certification(c echo.Context)error{

	return c.File("Inquery_certification.html")
}
//四类查询结果
func Inquery_info_result(c echo.Context)error{
	temp1 := c.FormValue("stuID")
	temp2, _ := strconv.ParseInt(temp1, 10, 64)
	stuID := uint64(temp2)
	u, err1:= client.StuInfoQuery(stuID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	log.Println("grpc: Inquery_info_result: success")
	log.Println("grade: ",u.Grade)
	return c.Render(http.StatusOK, "Inquery_info_result", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"major": u.Major,"grade":u.Grade,
	})
}
func Inquery_activity_result(c echo.Context)error{
	temp3 := c.FormValue("actID")
	temp4, _ := strconv.ParseInt(temp3, 10, 64)
	actID := uint32(temp4)
	u, err1:= client.ActInfoQuery(actID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	fmt.Println("grpc: Inquery_activity_result: success ",u)
	t := time.Unix(int64(u.Time), 0).Format("2006-01-02 03:04:05 PM")
	return c.Render(http.StatusOK, "Inquery_activity_result", map[string]interface{}{
		"actID":u.ActID,"actName": u.ActName, "sponsor": u.Organizer, "extInfo": u.ExtInfo, "time": t,
	})
}
func Inquery_grade_result(c echo.Context)error{
	temp1 := c.FormValue("stuID")
	temp2, _ := strconv.ParseInt(temp1, 10, 64)
	stuID := uint64(temp2)
	temp3 := c.FormValue("grade")
	temp4, _ := strconv.ParseInt(temp3, 10, 64)
	grade := uint32(temp4)
	u, err1:= client.StuGradeQuery(stuID, grade)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	fmt.Println("grpc: Inquery_grade_result: success")
	fmt.Println(u.StuName)
	t := time.Unix(int64(u.Time), 0).Format("2006-01-02 03:04:05 PM")
	return c.Render(http.StatusOK, "Inquery_grade_result", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"time":t,"extInfo": u.ExtInfo,"grade":grade,"averageGrades":u.AverageGrades,
		"obligatoryCredit":u.ObligatoryCredit,"optionalCredit":u.OptionalCredit,
	})
}
func Inquery_certification_result(c echo.Context)error{
	temp1 := c.FormValue("certID")
	temp2, _ := strconv.ParseInt(temp1, 10, 64)
	certID := uint64(temp2)
	u, err1:= client.StuCertQuery(certID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	log.Println(u)
	fmt.Println("grpc: Inquery_certification_result: success")
	
	t := time.Unix(int64(u.Time), 0).Format("2006-01-02 03:04:05 PM")
	return c.Render(http.StatusOK, "Inquery_certification_result", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"major": u.Major,
		"certID": u.CertID,"studyTime": u.StudyTime, "certstatus":u.CertStatus, "time":t,
		"extInfo":u.ExtInfo,
	})
}
//查询学分的页面
func Inquery_credit1(c echo.Context)error{

	return c.File("Inquery_credit1.html")
}
//查询学分的结果
func Inquery_credit1_result(c echo.Context)error{
	temp1 := c.FormValue("stuID")
	temp2, _ := strconv.ParseInt(temp1, 10, 64)
	stuID := uint64(temp2)

	u, err1:= client.AllCreditQuery(stuID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}

	u1, err2:= client.StuInfoQuery(stuID)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}

	log.Println("grpc: Inquery_credit1_result: success")
	return c.Render(http.StatusOK, "Inquery_credit1_result", map[string]interface{}{
		"stuID": u1.StuID, "stuName": u1.StuName,"usName":u1.UsName,"major": u1.Major,"grade":u1.Grade,
		"creditSum": u.CreditSum, "obligatoryCredit": u.ObligatoryCredit,"optionalCredit": u.OptionalCredit,
	})
}
//诚信查询
func credit(c echo.Context) error{
	return c.File("Inquery_credit.html")
}
func credit_result(c echo.Context) error{
	temp1 := c.FormValue("stuID")
	temp2, _ := strconv.ParseInt(temp1, 10, 64)
	stuID := uint64(temp2)
	u,err1 := client.CreditEvaluate(stuID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}

	u1, err2:= client.StuInfoQuery(stuID)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}

	t := time.Now().Format("2006-01-02 15:04:05")
	value := 666
	fmt.Println("grpc: Inquery_credit_result: success")

	u2, err3:= client.AllCreditQuery(stuID)
	if err3 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err3,})
	}

	return c.Render(http.StatusOK, "Inquery_credit_result", map[string]interface{}{
		"stuID": u1.StuID, "stuName": u1.StuName,"usName":u1.UsName,"major": u1.Major,"grade":u1.Grade,
		"usScore": u.UsScore,"gradeScore":u.GradeScore,"levelScore": u.LevelScore,"creditScore": u.CreditScore+u2.CreditSum/5,"time":t,"value":value,
		"credit":u2.CreditSum/5,
	})
}
//证书认证前查询的页面
func certification_check(c echo.Context)error{
	return c.File("Inquery_certification1.html")
}
//证书认证前的查询结果
func Inquery_certification_result1(c echo.Context)error{
	temp1 := c.FormValue("certID")
	temp2, _ := strconv.ParseInt(temp1, 10, 64)
	certID := uint64(temp2)
	u, err1:= client.StuCertQuery(certID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	log.Println(u)
	fmt.Println("grpc: Inquery_certification_result: success")
	
	t := time.Unix(int64(u.Time), 0).Format("2006-01-02 03:04:05 PM")
	return c.Render(http.StatusOK, "Inquery_certification_result1", map[string]interface{}{
		"stuID": u.StuID, "stuName": u.StuName,"usName":u.UsName,"major": u.Major,
		"certID": u.CertID,"studyTime": u.StudyTime, "certstatus":u.CertStatus, "time":t,
		"extInfo":u.ExtInfo,
	})
}
//证书认证
type Code struct {
	Result string
 }
func check(c echo.Context)error{
	temp1 := c.FormValue("certID")
	temp2, _ := strconv.ParseInt(temp1, 10, 64)
	certID := uint64(temp2)
	u, err1:= client.StuCertQuery(certID)
	if err1 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err1,})
	}
	log.Println("grpc:StuCertQuery success")
	str := c.FormValue("stuID")+c.FormValue("certID")+u.StuName+u.UsName+u.Major+u.StudyTime+u.CertStatus
	s := u.CertSignature
	log.Println("查询到的签名字符串：",s)
	log.Println("根据表单生成的字符串：",str)
	data, err2 := hex.DecodeString(s)
	if err2 != nil {
    	return c.Render(http.StatusOK, "show_err", map[string]interface{}{"err": err2,})
	}
	log.Println("公钥: ",u.ExtInfo)
	log.Println("签名字符串byte:  ", data)
	ifSuccess := rsa.VerifySign(u.ExtInfo,data,[]byte(str))
	log.Println(ifSuccess)
	// return c.Render(http.StatusOK, "show_success", map[string]interface{}{"ifSuccess":ifSuccess,})
	code := &Code{
		Result : "1",
	 }
	 //return c.Render(http.StatusOK, "show_success", map[string]interface{}{"ifSuccess":ifSuccess,})
	 return c.JSON(http.StatusOK, code)
}
func main(){

	e := echo.New()

	t := &Template{
		templates: template.Must(template.ParseGlob("*.html")),
	  }

	e.Renderer = t

	e.Static("/assets", "assets")
	e.Static("/css", "css")
	e.Static("/fonts", "fonts")
	e.Static("/img", "img")
	e.Static("/scripts", "scripts")
	e.Static("/styles", "styles")
	e.Static("/person/assets", "person/assets")
	e.Static("/firm_assets", "firm/assets")
	e.Static("/firm_act/assets", "firm/act/assets")
	e.Static("/school/assets", "school/assets")
	e.Static("/school/act/assets", "school/act/assets")
	e.Static("/school/cert/assets", "school/cert/assets")
	e.Static("/school/grade/assets", "school/grade/assets")
	e.Static("/school/stu/assets", "school/stu/assets")

	e.GET("/index",index)
	e.GET("/",index)
	e.GET("/firm",firm)
	e.GET("/school",school)
	e.GET("/person",person)

	e.GET("/firm_act_info",firm_act_info)
	e.GET("/firm_act_update",firm_act_update)
	e.GET("/firm_act_update_stu",firm_act_update_stu)
	e.GET("/firm_query",firm_query)

	e.GET("/school/query",school_query)
	e.GET("/school/act/info",school_act_info)
	e.GET("/school/act/update",school_act_update)
	e.GET("/school/act/update_stu",school_act_update_stu)
	e.GET("/school/cert/info",school_cert_info)
	e.GET("/school/cert/update",school_cert_update)
	e.GET("/school/grade/update_stu",school_grade_update_stu)
	e.GET("/school/stu/update",school_stu_update)
	e.GET("/school/stu/info",school_stu_info)
	//学生活动操作
	e.GET("/activity_info_input",activity_info_input)
	e.GET("/activity_info_update",activity_info_update)
	e.GET("/act_info_inquery",act_info_inquery)
	e.GET("/show_stu_activity",show_stu_activity)
	//学生信息查询
	e.GET("/firm_query",firm_query)
	//学生信息更新
	e.GET("/person_update_before",person_update_before)
	//学生活动更新
	e.GET("/school_act",school_act)
	//学生活动更新写入链
	e.GET("/write_update_activity",write_update_activity)
	//证书发布前
	e.GET("/before_cert_info",before_cert_info)
	//证书发布
	e.GET("/cert_info",cert_info)
	//证书更新
	e.GET("/cert_update",cert_update)
	//证书更新
	e.GET("/cert_update_data",cert_update_data)
	//初始证书到链上
	e.GET("/write_cert_info",write_cert_info)
	//成绩更新前的查询
	e.GET("/grade_info1",grade_info1)
	//成绩更新
	e.GET("/grade_info",grade_info)
	//学生信息更新
	e.GET("/person_update",person_update)
	//写入成绩信息到链上
	e.GET("/write_grade",write_grade)
	//个人信息初始化
	e.GET("/person_info",person_info) 
	//更新证书
	e.GET("/write_certification_update",write_certification_update)
	//更新学生信息的页面
	e.GET("/info_update",info_update)  
	//写入更新的学生信息到链上
	e.GET("/write_stu_info",write_stu_info)
	//跳转到学生信息初始化页面
	e.GET("/initial_stu_info",initial_stu_info)
	//初始化学生信息写入链
	e.GET("/write_stu_init",write_stu_init)
	//四种查询的页面
	e.GET("/Inquery_info",Inquery_info)
	e.GET("/Inquery_activity",Inquery_activity)
	e.GET("/Inquery_grade",Inquery_grade)
	e.GET("/Inquery_certification",Inquery_certification)
	//四种查询的结果
	e.GET("/Inquery_info_result",Inquery_info_result)
	e.GET("/Inquery_activity_result",Inquery_activity_result)
	e.GET("/Inquery_grade_result",Inquery_grade_result)
	e.GET("/Inquery_certification_result",Inquery_certification_result) 
	//查询学分的页面
	e.GET("/Inquery_credit1",Inquery_credit1)
	//查询学分的结果
	e.GET("/Inquery_credit1_result",Inquery_credit1_result)
	//诚信查询
	e.GET("/credit",credit)  
	e.GET("/credit_result",credit_result)
	//证书认证之前的查询
	e.GET("/certification_check",certification_check)  
	//证书认证之前的查询返回
	e.GET("/Inquery_certification_result1",Inquery_certification_result1)
	//证书认证过程
	e.GET("/check",check)
	e.GET("/before_actgrade",before_actgrade)
	e.GET("/actgrade",actgrade)
	e.GET("/query_actgrade",query_actgrade)
	e.GET("/query_actgrade_result",query_actgrade_result)
	e.Logger.Fatal(e.Start(":10005")) 
}