new Vue({
    el: "#app",
    methods: {
        loginPE(){
            console.log(this.user);
            alert('submit1');
        },
        onSubmit() {
            alert('submit1');
        },
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        onTrade(){
            axios.post("/credit", this.creditAgent).then((res)=>{
                console.log(this.creditAgent);
            })
        },
        onTradeB(){
            axios.post("/credit",this.creditAgent).then((res)=>{
                console.log(this.creditAgent);
            })
        }

    },
    data() {
        return {
            form: {
                name: '',
                pwd: ''
            },
            sizeForm: {
                name: '',
                region: '',
                date1: '',
                date2: '',
                delivery: false,
                type: [],
                resource: '',
                desc: ''
            },
            creditAgent:{
                balance: '',
                addressTo: '',
                value: '',
                data: null
            },
            medical:{
                type: '',
                status:''
            },
            user:{
                role: '',
                account: '',
                pwd: ''
            }

        };

    }
})