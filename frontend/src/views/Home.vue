<template>
    <div class="home">
        <b-sidebar id="sidebar-right" title="Options" right shadow>
            <div class="search">
                <input v-model="searchTerm" placeholder="Search"/>
                <br><br>
                <div v-if="!activeSearch">
                    <b-button-group>
                        <b-button size="sm" v-for="(btn, idx) in buttons" :key="idx" :pressed.sync="btn.state" variant="primary" v-on:click="toggleSearch(btn.caption)"> {{btn.caption}}</b-button>
                    </b-button-group><br><br>
                    <b-button  variant="primary"  v-on:click="search()"> Go</b-button>
                </div>
                <div v-if="activeSearch">
                    <b-button size="sm" variant="primary" v-on:click="clearSearch()">Clear Search</b-button>
                </div>
                
            <br><br>
            <h5>Sort by</h5>
            <b-button-group>
                <b-button size="sm" v-for="(tgl, idx) in toggles" :key="idx" :pressed.sync="tgl.state" variant="primary" v-on:click="toggleSearch(tgl.caption)"> {{tgl.caption}}</b-button>
            </b-button-group>
            <br><br>
            <h5>Help:</h5>
            <div class="helptext">
                <ul>
                    <li>To view finished To-Dos, go to 'To-Dos', then 'Manange all To-Dos'.</li>
                    <li>To change Data including the finished status, got to 'To-Dos', then 'Manage all To-Dos' and edit the To-Do.</li>
                    <li>To create a new To-Do, got to 'To-Dos', then 'New To-Do'.</li>
                    <li>Only existing Users can be assigned to a To-Do.</li>
                    <li>To create a new Assignee, go to 'Assignees', then 'New Assignee'.</li>
                </ul>

            </div>
            
            </div>

            
        </b-sidebar>
        <div class="column left">
            <div  class="ToDoBox" v-for="toDo in displayToDos" v-bind:key="toDo.id">
                
                <h3>{{toDo.title}}</h3>
                Number of Assignees: {{toDo.assigneeList.length}} <br>
                <div v-if="toDo.hasDueDate">
                Due Date: 
                <b-form-datepicker  v-model="toDo.dueDateString" :disabled="true"/>
                <b-form-timepicker  v-model="toDo.dueTimeValue" locale="de" :disabled="true"/><br>
                </div>
                <b-button v-if="!toDo.finished" size="sm" :variant="toDo.variant" v-on:click="finish(toDo.id)">
                   Finish {{toDo.finishButtonTextExtention}}

                    </b-button>
                <hr style="height:0px;border-width:0;"> 
            </div>
        </div>
        <div class="column right">
            <b-button v-b-toggle.sidebar-right variant="primary">Options</b-button>
        </div>
    </div>
</template>

<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";
export default {
    name: "Home",

    data() {
        return {
           toDos:[],
           unfinishedToDos:[],
           displayToDos:[],
           activeSearch:false,
           data:{},
           searchTerm: "",
           buttons: [
               { caption: 'Search unfinished', state: true},
               { caption: 'Search all', state:false}
           ],
           toggles: [
               {caption: 'Due Date', state: true},
               {caption: 'Title', state: false}
           ],
           
        };
    },
    computed:{
        //buttons for search
        btnStates(){
            return this.buttons.map(btn=>btn.state)
        },
        //buttons for sort
        tglStates(){
            return this.toggles.map(tgl=>tgl.state)
        }
    },
    methods: {

        // this function is assigned to the button
        fetchAllToDos: function () {
            axios.get(`${config.apiBaseUrl}/todos`).then((response) => {
                // log response data to the browser console
                //console.log(response.data);
                // assign response to data variable
                this.toDos=response.data
                //this.toDos = response.data;
                this.toDos.forEach(toDo => {
                    toDo['variant']='primary';
                    toDo['finishButtonTextExtention']=''
                    if(toDo.dueDate==0){
                        toDo['hasDueDate']=false
                        
                    }else{
                        toDo['hasDueDate']=true
                        let now=new Date();
                        if(Date.parse(now)>toDo.dueDate){
                            toDo.finishButtonTextExtention='(overdue)'
                            //if overdue
                            toDo.variant='danger'
                        }else if(Date.parse(now)+86400000>toDo.dueDate){
                            //if under 24h remain
                            toDo.variant='warning'
                            toDo.finishButtonTextExtention= '(due soon)'
                        }
                    }
                    //Change UTC Time code to readable data with local systems time
                    //console.log(toDo.finishedDate)
                    toDo['dueDateString']= new Date(toDo.dueDate)
                    //console.log(toDo.finishedDate)
                    let timeString =toDo.dueDateString.toTimeString()

                    //Only need the first part of time (e.g): 04:02:00 GMT+0100 (Mitteleuropäische Normalzeit)
                    let timeArray=timeString.split(" ");
                    //console.log(finishedTimeString)

                    toDo['dueTimeValue']= timeArray[0]
                    //console.log(finishedTimeArray[0])
                    if(toDo.finished==false){
                        this.unfinishedToDos.push(toDo)
                    }
                });
                this.displayToDos=this.unfinishedToDos
            });
        },
        search: function(){
            if(this.buttons[0].state==true){
                this.activeSearch=true
                this.displayToDos=this.unfinishedToDos.filter((toDo) => toDo.title.includes(this.searchTerm))
            }else {
                this.activeSearch=true
                this.displayToDos=this.toDos.filter((toDo) => toDo.title.includes(this.searchTerm))
            }

        },
        clearSearch: function(){
            this.activeSearch=false
            this.displayToDos=this.unfinishedToDos
        },
        toggleSearch: function(id){
            //switch other button off
            if(id=="Search unfinised"){
                this.buttons[1].state=false
            }else if (id=="Search all"){
                this.buttons[0].state=false
            }else if(id=="Due Date"){
                this.toggles[1].state=false
                this.sortByDate()
            }else if(id=='Title'){
                this.toggles[0].state=false
                this.sortByTitle()
                
            }

        },
        sortByDate: function(){
            this.displayToDos.sort(function(a,b){
                var aDate=a.dueDateString
                aDate.setHours(a.dueTimeValue.split(":")[0])
                aDate.setMinutes(a.dueTimeValue.split(":")[1])
                var bDate=b.dueDateString
                bDate.setHours(b.dueTimeValue.split(":")[0])
                bDate.setMinutes(b.dueTimeValue.split(":")[1])
                var aUTC=Date.parse(aDate)
                var bUTC=Date.parse(bDate)
                if(aUTC<bUTC){
                    return -1
                }if(aUTC>bUTC){
                    return 1
                }return 0
            })
        },
        sortByTitle: function(){
            this.displayToDos.sort(function(a,b){
                var titleA =a.title.toUpperCase()
                var titleB=b.title.toUpperCase()
                if(titleA<titleB){
                    return -1
                }if (titleA>titleB){
                    return 1
                }return 0

            })
        },
        finish: function(id){
                let data ={}
                this.unfinishedToDos.forEach(toDo => {
                    if(toDo.id==id){
                        data=toDo
                    }
                });
                //console.log(data) 
                data.finished=true
                let assigneeIdList= []
                data.assigneeList.forEach(assignee => {
                    assigneeIdList.push(assignee.id)
                });
                //console.log(assigneeIdList)
                data['assigneeIdList']=assigneeIdList
                axios.put(`${config.apiBaseUrl}/todos/${id}`, data).then(()=>{
                //console.log(response.data);
                //console.log(data.finishedDate)
                this.unfishedToDos = this.unfinishedToDos.filter((toDo) => toDo.id !== id);
                this.displayToDos = this.displayToDos.filter((toDo) => toDo.id !== id)
            });
        },
        showToastMessage(title, msg, variant) {
            this.$bvToast.toast(msg, {
                title: title,
                variant: variant,
                solid: true,
                toaster: "b-toaster-top-center",
                autoHideDelay: 4000,
                appendToast: true
            });
        }
    },
    created: function(){
        this.fetchAllToDos();
    }
};
</script>

<style>
.home label {
    margin-right: 10px;
    height: 100%;
}
.column{
    float: left;
    
}
.left{
    width: 95%;
}
.right {
    width: 5%;
    text-align: right;
    position: fixed;
    right: 10px;
    top: 75px;
}
.search{
    text-align: center ;
}
.helptext{
    border: 1px solid cornflowerblue;
    text-align: left;
    margin: 10px;
}

</style>
