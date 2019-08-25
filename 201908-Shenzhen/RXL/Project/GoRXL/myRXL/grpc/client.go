package client

import(
//grpc相关包
	"context"
	"log"
	"google.golang.org/grpc"
	"fmt"
	pb "connect"
)
//学生信息数据结构
type StuInfo struct{
	StuID uint64 `json:"stuID"`
	StuName string `json:"stuName"`
	UsName string `json:"usName"`
	Major string `json:"major"`
	ExtInfo string `json:"extInfo"`
	Time uint64 `json:"Time"`
}
//学生成绩数据结构
type GradeInfo struct{
	StuID uint64 `json:"stuID"`
	StuName string `json:"stuName"`
	Term uint32 `json:"term"`
	AverageGrades uint32 `json:"averageGrades"`
	ExtInfo string `json:"extInfo"`
	Time uint64 `json:"Time"`
}
//活动发布信息数据结构
type ActivityInfo struct {
	ActID uint32  `json:"actID"`
    ActName string  `json:"actName"`
	Sponsor string  `json:"sponsor"`
	Status string  `json:"status"`
	ExtInfo string  `json:"extInfo"`
	Time uint64  `json:"Time"`
	}
//学生活动记录数据结构
type ActivityRecode struct{
	ActID uint32 `json:"actID"`
	StuID uint64 `json:"stuID"`
	ActName string `json:"actName"`
	StuName string `json:"stuName"`
	ExtInfo string `json:"extInfo"`
	Time uint64 `json:"Time"`
	ActSignature string `json:"actSignature"`
}
//学历证书数据结构
type Certification struct{
	CertID uint64 `json:"certID"`
	StuID uint64 `json:"stuID"`
	StuName string `json:"stuName"`
	UsName string `json:"usName"`
	Major string `json:"major"`
	StudyTime string `json:"studyTime"`
	CertStatus string `json:"certStatus"`
	ExtInfo string `json:"extInfo"`
	Time uint64 `json:"Time"`
	CertSignature string `json:"certSignature"`
}

/*-------------------------------grpc客户端--------------------------------------*/

func StuInfoInit(u StuInfo) (string, error) {
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.StuInfoInitInput{}
	i.StuID = u.StuID
	i.StuName = u.StuName
	i.UsName = u.UsName
	i.Major = u.Major
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	//client调用rpc
	resp, err := client.StuInfoInit(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	response := resp.GetSuccess()
	return response, err
}

func StuInfoUpdate(u StuInfo) (string, error){
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.StudentInfoUpdateInput{}
	i.StuID = u.StuID
	i.StuName = u.StuName
	i.UsName = u.UsName
	i.Major = u.Major
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	//client调用rpc
	resp, err := client.StuInfoUpdate(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	response := resp.GetSuccess()
	return response, err

}

func StuGradeRecode(u GradeInfo) (string, error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.StuGradeRecodeInput{}
	i.StuID = u.StuID
	i.StuName = u.StuName
	i.Term = u.Term
	i.AverageGrades = u.AverageGrades
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	//client调用rpc
	resp, err := client.StuGradeRecode(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	response := resp.GetSuccess()
	return response, err

}

func StuGradeUpdate(u GradeInfo) (string, error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.StuGradeUpdateInput{}
	i.StuID = u.StuID
	i.StuName = u.StuName
	i.Term = u.Term
	i.AverageGrades = u.AverageGrades
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	//client调用rpc
	resp, err := client.StuGradeUpdate(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	response := resp.GetSuccess()
	return response, err

}

func ActivityRegister(u ActivityInfo) (string, error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.ActivityRegisterInput{}
	i.ActID = u.ActID
	i.ActName = u.ActName
	i.Sponsor = u.Sponsor
	i.Status = u.Status
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	//client调用rpc
	resp, err := client.ActivityRegister(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	response := resp.GetSuccess()
	return response,err
}

func ActivityInfoUpdate(u ActivityInfo) (error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.ActivityInfoUpdateInput{}
	i.ActID = u.ActID
	i.ActName = u.ActName
	i.Sponsor = u.Sponsor
	i.Status = u.Status
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	//client调用rpc
	resp, err := client.ActivityInfoUpdate(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	response := resp.GetSuccess()
	fmt.Println(response)
	return err
}
	
func ActivityGradeRecode(u ActivityRecode) (string,error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.ActivityGradeRecodeInput{}
	i.ActID = u.ActID
	i.StuID = u.StuID
	i.ActName = u.ActName
	i.StuName = u.StuName
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	i.ActSignature = u.ActSignature
	//client调用rpc
	resp, err := client.ActivityGradeRecode(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	response := resp.GetSuccess()
	return response,err
}

func CertInfoInit(u Certification) (string,error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.CertInfoInitInput{}
	i.CertID = u.CertID
	i.StuID = u.StuID
	i.StuName = u.StuName
	i.UsName = u.UsName
	i.Major = u.Major
	i.StudyTime = u.StudyTime
	i.CertStatus = u.CertStatus
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	i.CertSignature = u.CertSignature
	//client调用rpc
	resp, err := client.CertInfoInit(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	response := resp.GetSuccess()
	return response,err
}

func CertInfoUpdate(u Certification) (string,error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var i = pb.CertInfoUpdateInput{}
	i.CertID = u.CertID
	i.StuID = u.StuID
	i.StuName = u.StuName
	i.UsName = u.UsName
	i.Major = u.Major
	i.StudyTime = u.StudyTime
	i.CertStatus = u.CertStatus
	i.ExtInfo = u.ExtInfo
	i.Time = u.Time
	i.CertSignature = u.CertSignature
	//client调用rpc
	resp, err := client.CertInfoUpdate(context.Background(), &i)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	response := resp.GetSuccess()
	return response,err
}

func StuInfoQuery(StuID uint64) (StuInfo,error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var r = pb.StuInfoQueryInput{}
	r.StuID = StuID
	//client调用rpc
	resp, err := client.StuInfoQuery(context.Background(), &r)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	var i = StuInfo{}
	i.StuID = resp.GetStuID()
	i.StuName = resp.GetStuName()
	i.UsName = resp.GetUsName()
	i.Major = resp.GetMajor()
	i.ExtInfo = resp.GetExtInfo()
	i.Time = resp.GetTime()
	return i,err
}

func StuGradeQuery(StuID uint64,Term uint32) (GradeInfo,error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var r = pb.StuGradeQueryInput{}
	r.StuID = StuID
	r.Term = Term
	//client调用rpc
	resp, err := client.StuGradeQuery(context.Background(), &r)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	var i = GradeInfo{}
	i.StuID = resp.GetStuID()
	i.StuName = resp.GetStuName()
	i.Term = resp.GetTerm()
	i.AverageGrades = resp.GetAverageGrades()
	i.ExtInfo = resp.GetExtInfo()
	i.Time = resp.GetTime()
	return i,err
}

func StuActQuery(StuID uint64,ActID uint32) (ActivityRecode,error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var r = pb.StuActQueryInput{}
	r.StuID = StuID
	r.ActID = ActID
	//client调用rpc
	resp, err := client.StuActQuery(context.Background(), &r)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	var i = ActivityRecode{}
	i.ActID = resp.GetActID()
	i.StuID = resp.GetStuID()
	i.ActName = resp.GetActName()
	i.StuName = resp.GetStuName()
	i.ExtInfo = resp.GetExtInfo()
	i.Time = resp.GetTime()
	i.ActSignature = resp.GetActSignature()
	return i,err
}

func StuCertQuery(CertID uint64) (Certification,error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var r = pb.StuCertQueryInput{}
	r.CertID = CertID
	//client调用rpc
	resp, err := client.StuCertQuery(context.Background(), &r)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	var i = Certification{}
	i.CertID = resp.GetCertID()
	i.StuID = resp.GetStuID()
	i.StuName = resp.GetStuName()
	i.UsName = resp.GetUsName()
	i.Major = resp.GetMajor()
	i.StudyTime = resp.GetStudyTime()
	i.CertStatus = resp.GetCertStatus()
	i.ExtInfo = resp.GetExtInfo()
	i.Time = resp.GetTime()
	return i,err
}

func ActInfoQuery(ActID uint32) (ActivityInfo,error){
	//初始化tcp连接
	conn, err := grpc.Dial(":50051", grpc.WithInsecure())
	if err != nil {
		log.Printf("grpc.Dial err: %v", err)
	}
	defer conn.Close()
	//新建client对象
	client := pb.NewCreditCertClient(conn)
	//新建proto的input对象
	var r = pb.ActInfoQueryInput{}
	r.ActID = ActID
	//client调用rpc
	resp, err := client.ActInfoQuery(context.Background(), &r)
	if err != nil {
		log.Printf("client.Search err: %v", err)
	}
	//读取返回的数据	
	var i = ActivityInfo{}
	i.ActID = resp.GetActID()
	i.ActName = resp.GetActName()
	i.Sponsor = resp.GetSponsor()
	i.ExtInfo = resp.GetExtInfo()
	i.Time = resp.GetTime()
	return i,err
}

