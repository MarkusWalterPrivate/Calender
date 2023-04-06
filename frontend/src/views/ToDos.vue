<template>
    <div>
        <b-sidebar id="sidebar-right" title="Options" right shadow>
            <div class="search">
                <input v-model="searchTerm" placeholder="Search"/>
                <br><br>
                <div v-if="!activeSearch">
                    <b-button-group>
                        <b-button size="sm" v-for="(btn, idx) in buttons" :key="idx" :pressed.sync="btn.state" variant="primary" v-on:click="toggleSearch(btn.caption)"> {{btn.caption}}</b-button>
                    </b-button-group><br><br>
                    <b-button  variant="primary"  v-on:click="search()"><b-icon-search/> Go</b-button>
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
            <div v-if="editMode==false">
                <b-button
                size="sm"
                variant="outline-primary"
                :pressed.sync="detailedMode">
                Toggle detailed Mode
                </b-button>
            </div>
            <br><br>
            <b-button size="sm" variant="primary" v-on:click="saveFile()"><b-icon-cloud-arrow-down/> Save ToDos as CSV</b-button>
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
    
        <h1 >To-Dos</h1>
        <div>
            <b-alert v-bind:show="toDos.length === 0" variant="warning">
                No To-Dos available on the server...
            </b-alert>
            <!-- what is shown when To-Dos are only listed-->
        <div v-if="editMode==false&&detailedMode==false">
            <div  class="ToDoBox" v-for="toDo in displayToDos" v-bind:key="toDo.id">
                
                <h3>{{toDo.title}}</h3>
                Number of Assignees: {{toDo.assigneeList.length}} <br>
                <div v-if="toDo.hasDueDate">
                    Due Date: 
                    <b-form-datepicker v-model="toDo.dueDate" :disabled="true"/>
                    <b-form-timepicker v-model="toDo.dueTimeValue" locale="de" :disabled="true"/> 
                </div>
                <div v-if="!toDo.hasDueDate">
                    No Due Date
                </div>
                Finished? <input type="checkbox" v-model="toDo.finished" :disabled="true"/>
                <hr style="height:0px;border-width:0;"> 

                    <b-button
                        size="sm"
                        variant="outline-danger"
                        v-on:click="deleteToDo(toDo.id)"
                        justify= "space around"
                    >
                        <b-icon-trash></b-icon-trash> Delete
                    </b-button> &nbsp;
                    <b-button
                        size="sm"
                        variant="warning"
                        v-on:click="editToDo(toDo.id)"
                        justify= "space around"
                    >
                        Edit
                    </b-button>
   
            </div>
        </div>
        <!-- what is shown when a To-Dos is in detailed view-->
        <div v-if="detailedMode&&editMode==false">
            <div  class="ToDoBox" v-for="toDo in displayToDos" v-bind:key="toDo.id">
                
                <h3>{{toDo.title}}</h3>
                {{toDo.description}}<br><br>
                Assigned Members: 
                <div v-for="assignee in toDo.assigneeList" v-bind:key="assignee.id">
                    {{assignee.prename}} {{assignee.name}}, {{assignee.email}}
                </div>
                 <br>
                 <div v-if="toDo.hasDueDate">
                Due Date: 
                <b-form-datepicker v-model="toDo.dueDate" :disabled="true"/>
                <b-form-timepicker v-model="toDo.dueTimeValue" locale="de" :disabled="true"/>
                 </div>
                <div v-if="!toDo.hasDueDate">
                    No Due Date
                </div>
                <br>
                Finished? <input type="checkbox" v-model="toDo.finished" :disabled="true"/>
                <br><br>
                <div v-if="toDo.finished">
                    Finished: <br>
                    <b-form-datepicker v-model="toDo.finishedDate" :disabled="true"/>
                    <b-form-timepicker v-model="toDo.finishedTimeValue" locale="de" :disabled="true"/>
                    <br> 
                </div>
            
                Created: <br>
                <b-form-datepicker v-model="toDo.createdDate" :disabled="true"/>
                <b-form-timepicker v-model="toDo.createdTimeValue" locale="de" :disabled="true"/>
                <br>

                <hr style="height:0px;border-width:0;">
                    <b-button
                        size="sm"
                        variant="outline-danger"
                        v-on:click="deleteToDo(toDo.id)"
                        justify= "space around"
                    >
                        <b-icon-trash></b-icon-trash> Delete
                    </b-button> &nbsp;
                    <b-button
                        size="sm"
                        variant="warning"
                        v-on:click="editToDo(toDo.id)"
                        justify= "space around"
                    >
                        Edit
                    </b-button>
   
            </div>
        </div>
            <!-- what is shown when a Todo is edited-->
            <div v-if="editMode" class="ToDoBox"  v-bind:key="eAssignee.id">
                <form>
                    <div class="form-group">
                        Title: 
                        <input
                            v-model="eAssignee.title"
                            class="form-control"
                        />
                    </div>    
                    <div class="form-group">
                        Decription: 
                        <textarea
                            v-model="eAssignee.description"
                            class="form-control"
                        />
                    </div>
                    Assigned Members:
                    <div v-for="assignee in assignees" v-bind:key="assignee.id">
                    <input type="checkbox" :id="assignee.id" :value="assignee.id" v-model="selectedAssignees">
                    <label :for="assignee.id"> &nbsp; {{assignee.prename}} {{assignee.name}} </label>
                  
                    </div>
                    <div v-b-popover.hover="'Disselect to delete the due date. Setting a due date without selecting will not save due date'">
                        Has due date?
                    <input type="checkbox" v-model="eAssignee.hasDueDate">
                    </div>
                    <div class="form-group">
                        Due Date:
                    <b-form-datepicker v-model="eAssignee.dueDate" :min="min"/>
                    <b-form-timepicker v-model="eAssignee.dueTimeValue" locale="de"/>
                    </div>
                    Finished: <input type="checkbox" v-model="eAssignee.finished"> <br>
                <b-button variant= "warning" v-on:click="saveEdits(eAssignee.id)" >Save Changes</b-button>&nbsp;
                <b-button variant= "outline-danger" v-on:click="discard()" >Discard Changes</b-button>   
                </form>
            </div>
        </div>
        
    </div>
            <div class="column right">
            <b-button variant="primary" href="/#/createToDo">New ToDo</b-button><br><br>
            <b-button v-b-toggle.sidebar-right variant="primary">Options</b-button>
        </div>
    </div>
</template>

<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";
import {saveAs} from 'file-saver';

export default {
    name: "Home",
    data() {
        const today = new Date 
        const minDate= new Date(today)
        return {
            min: minDate,
            toDos: [],
            assignees: [],
            eAssignee:{},
            deleteMessage: "",
            editMode: false,
            selectedAssignees:[],
            title:"",
            description: "",
            testDate: new Date,
            dueTimeValue: '',
            dateValue: "",
            detailedMode: false,
            buttons: [
               { caption: 'Search unfinished', state: true},
               { caption: 'Search all', state:false}
           ],
           toggles: [
               {caption: 'Due Date', state: true},
               {caption: 'Title', state: false}
           ],
           searchTerm: "",
           activeSearch:false,
           displayToDos:[],
           unfinishedToDos:[]
        };
    },
    computed:{
        displayToDo(){
            return this.toDos
        }
    },
    methods: {
        // save ToDos as csv
        saveFile: function() {
            let file;
            axios.get(`${config.apiBaseUrl}/csv-downloads/todos`).then((response) => {
                file= new File([response.data], "ToDos.csv", {type: "application/csv;charset=utf-8"});
                //console.log(file)
                saveAs(file)
            })

        },
        fetchAllToDos: function () {
            axios.get(`${config.apiBaseUrl}/todos`).then((response) => {
                // log response data to the browser console
                //console.log(response.data);
                // assign response to data variable
                this.toDos = response.data;
                this.toDos.forEach(toDo => {
                    if(toDo.dueDate==0){
                        toDo['hasDueDate']=false
                    }else{
                        toDo['hasDueDate']=true
                    }
                    
                    //Change UTC Time code to readable data with local systems time
                    //console.log(toDo.finishedDate)
                    toDo.dueDate= new Date(toDo.dueDate)
                    toDo.createdDate =new Date(toDo.createdDate)
                    toDo.finishedDate=new Date(toDo.finishedDate)
                    //console.log(toDo.finishedDate)
                    let timeString =toDo.dueDate.toTimeString()
                    let createdTimeString =toDo.createdDate.toTimeString()
                    let finishedTimeString= toDo.finishedDate.toTimeString()
                    //Only need the first part of time (e.g): 04:02:00 GMT+0100 (Mitteleuropäische Normalzeit)
                    let timeArray=timeString.split(" ");
                    let createdTimeArray=createdTimeString.split(" ")
                    let finishedTimeArray=finishedTimeString.split(" ")
                    //console.log(finishedTimeString)

                    toDo['dueTimeValue']= timeArray[0]
                    toDo['createdTimeValue']= createdTimeArray[0]
                    toDo['finishedTimeValue']= finishedTimeArray[0]
                    //console.log(finishedTimeArray[0])
                    if(toDo.finished==false){
                        this.unfinishedToDos.push(toDo)
                    }
                    
                });
                this.displayToDos=this.toDos
            });
        },
        fetchAllAssignees: function () {
            axios.get(`${config.apiBaseUrl}/assignees`).then((response) => {
                // log response data to the browser console
                //console.log(response.data);
                // assign response to data variable
                this.assignees = response.data;
                //this.$store.commit('setAssignees', response.data);
                //console.log(this.$store.state.assignees)
            });
        },
        deleteToDo: function (id) {
            this.$bvModal.msgBoxConfirm('Please confirm that you want to delete this ToDo', {
                title: 'Please Confirm',
                 size: 'sm',
                buttonSize: 'sm',
                okVariant: 'danger',
                okTitle: 'YES',
                cancelTitle: 'NO',
                footerClass: 'p-2',
                hideHeaderClose: false,
                centered: true
            }).then(value => {
            if(value){
            // send DELETE request to API to delete a specific cat by ID
            axios.delete(`${config.apiBaseUrl}/todos/${id}`).then(() => {
                // update view by removing deleted cat
                this.toDos = this.toDos.filter((toDo) => toDo.id !== id);
                this.displayToDos = this.displayToDos.filter((toDo) => toDo.id !== id);
                this.unfinishedToDos = this.unfinishedToDos.filter((toDo) => toDo.id !== id);
                // show success message using BootstrapVue toast component
                
                this.showToastMessage(
                    "Alert",
                    `Successfully deleted To-Do with ID ${id}!`,
                    "success"
                    
                );
            });
            }
            })
          .catch(err => {
            console.log(err)
          })
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
            this.displayToDos=this.toDos
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
                var aDate=a.dueDate
                aDate.setHours(a.dueTimeValue.split(":")[0])
                aDate.setMinutes(a.dueTimeValue.split(":")[1])
                var bDate=b.dueDate
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
        editToDo: function(id){
            axios.get(`${config.apiBaseUrl}/todos/${id}`).then((response) => {
                this.editMode= true
                // update view by removing deleted cat
                // show success message using BootstrapVue toast component
                this.eAssignee = response.data;
                if(this.eAssignee.dueDate==0){
                    this.eAssignee['hasDueDate']=false
                    this.eAssignee.dueDate= new Date()
                    this.eAssignee['dueTimeValue']= "12:00"
                }else{
                    this.eAssignee['hasDueDate']=true
                    this.eAssignee.dueDate= new Date(this.eAssignee.dueDate)
                    let dueTimeString =this.eAssignee.dueDate.toTimeString()
                    let dueTimeArray=dueTimeString.split(" ");
                    this.eAssignee['dueTimeValue']= dueTimeArray[0]
                }
                

                //Only need the first part of time (e.g): 04:02:00 GMT+0100 (Mitteleuropäische Normalzeit)
                   

                this.eAssignee.assigneeList.forEach(assignee => {
                    this.selectedAssignees.push(assignee.id)
                    
                });
                //console.log(response.data);
                //console.log(this.eAssignee)
            });
        },
        setTime: function(){
            //This would be much easier if the date and time were one field, but there is no combined date-time component and I am NOT gonna create one...

            //console.log(this.eAssignee.dueTimeValue)
            //console.log(this.eAssignee.dateValue)
            //console.log(typeof this.eAssignee.dueDate)
            if(typeof this.eAssignee.dueDate== "string"){
                let dateArray=this.eAssignee.dueDate.split("-");
                // console.log(dateArray)
                 this.testDate.setFullYear(dateArray[0])
                //WHY THE F*** DO ONLY!!!! MONTHS START AT 0??????
                this.testDate.setMonth(dateArray[1]-1)
                this.testDate.setDate(dateArray[2])
            }else{
                this.testDate= this.eAssignee.dueDate
            }

            let timeArray=this.eAssignee.dueTimeValue.split(":");
            //console.log(timeArray)
            this.testDate.setHours(timeArray[0])
            this.testDate.setMinutes(timeArray[1])
            //for nice round dates
            this.testDate.setSeconds(0)
            //console.log(this.testDate)
            //console.log(Date.parse(this.testDate))
            //this.testDate= new Date
            //this.tDate.setDate(this.date)
        },
        saveEdits: function(id){
            this.setTime()
            //console.log(this.eAssignee.finished)
            let data;
            if(this.eAssignee.hasDueDate){
                data = {'title': this.eAssignee.title, 'description': this.eAssignee.description, 'assigneeIdList': this.selectedAssignees, 'dueDate': Date.parse(this.testDate), 'finished': this.eAssignee.finished}
            }else{
                data = {'title': this.eAssignee.title, 'description': this.eAssignee.description, 'assigneeIdList': this.selectedAssignees, 'dueDate': 0, 'finished': this.eAssignee.finished}
            } 
            axios.put(`${config.apiBaseUrl}/todos/${id}`, data).then((response) => {
                this.editMode= true
                // update view by removing deleted cat
                // show success message using BootstrapVue toast component
                this.eAssignee = response.data;
                console.log(response.data);
                if(response.status==200){
                    this.showToastMessage(
                        "Success",
                        `Successfully saved changes to assignee with ID ${response.data.id}!`,
                        "success"
                    )
                    //leave edit mode and flush data
                    this.discard()
                    // update local data by requesting it again from server
                    this.fetchAllToDos();
     
                }
            }).catch((error) =>{
                console.log(error.response.data.message )
                console.log(error.message)
                this.showToastMessage(
                    error.message,
                    error.response.data.message,
                    "danger"
                )
            });
            
            


        },
        discard: function(){
            //leave edit mode and flush data
            this.editMode= false
            this.eAssignee={}
            this.selectedAssignees=[]
            this.name=""
            this.prename=""
            this.email=""
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
    // executed after the component has been started
    created: function () {
        this.fetchAllToDos();
        this.fetchAllAssignees();
    }
};
</script>

<style>
.ToDoBox {
    padding: 10px;
    margin: 3px;
    border: 1px solid cornflowerblue;
    float: left;
    min-width: 250px;
    text-align: center;
}

.catBox button {
    margin: 20px 5px 20px 20px;
    padding: 10px;
}
.column {
    float: left;
}
.left {
    width: 90%;
}
.right {
    width: 10%;
    text-align: right;
}
</style>