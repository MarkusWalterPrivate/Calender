<template>
    <div>
        <h1>Create a new To-Do</h1>
        <div>
            <form>
              <div class="form-group">
                  Title:
                <input
                  v-model="title"
                  placeholder="Title"
                  class="form-control"
                />
              </div>    
              <div class="form-group">
                Description: 
                <textarea
                v-model="description"
                placeholder="A brief description"
                class="form-control"
              ></textarea>

              </div>
              <div v-for="assignee in assignees" v-bind:key="assignee.id">
                  <input type=checkbox :id="assignee.id" :value="assignee.id" v-model="selectedAssignees">
                  <label :for="assignee.id"> {{assignee.prename}} {{assignee.name}} </label>
                  
              </div>
            Has due date?
            <input type="checkbox" v-model="hasDueDate">
            <div v-if="hasDueDate">
            <b-form-datepicker v-model="dateValue" :min="min"/>
            <b-form-timepicker v-model="timeValue" locale="de"/>
            </div>
            </form>
            <br>
            
        </div>
        <b-button variant= "primary" v-on:click="createToDo()" >Create To-Do</b-button> 
    </div>
</template>

<script>
// import configuration with API url; @ refers to the src directory
import config from "@/config";
// import library for HTTP requests
import axios from "axios";


export default {
    name: "Home",
    data(){
        const today = new Date 
        const minDate= new Date(today)
        return {
            assignees: [],
            selectedAssignees: [],
            title:"",
            description:"",
            //dueDate:"",
            deleteMessage: "",
            min: minDate,
            testDate : new Date,
            timeValue:'12:00:00',
            dateValue: new Date,
            hasDueDate: false
            
        };
    },
    computed:{
        displayDate(){

            return this.testDate
        }
    },
    methods: {
        // send GET request to API to fetch all assignees
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
        setTime: function(){
            if(this.hasDueDate){
            console.log(this.dateValue.toString())
            console.log(typeof this.dateValue)
            let timeArray=this.timeValue.split(":");
            if(typeof this.dateValue=="string"){
                //if date was set
                let dateArray=this.dateValue.split("-");
                this.testDate.setFullYear(dateArray[0])
                //WHY THE F*** DO ONLY!!!! MONTHS START AT 0??????
                this.testDate.setMonth(dateArray[1]-1)
                this.testDate.setDate(dateArray[2])
            }else{
                //in this case the dateValue is still a date and not a string and can be used directly
                this.testDate=this.dateValue
            }
            this.testDate.setHours(timeArray[0])
            this.testDate.setMinutes(timeArray[1])
            //for nice round dates
            this.testDate.setSeconds(0)
            //console.log(this.testDate)
            //console.log(Date.parse(this.testDate))
            //this.testDate= new Date
            //this.tDate.setDate(this.date)
            }

        },

        createToDo: function () {
            this.setTime()
            let data;
            if(this.hasDueDate){
                data = {'title': this.title, 'description': this.description, 'assigneeIdList': this.selectedAssignees, 'dueDate': Date.parse(this.testDate)}
            }else{
                data = {'title': this.title, 'description': this.description, 'assigneeIdList': this.selectedAssignees, 'dueDate': 0}
            }
            
            axios.post(`${config.apiBaseUrl}/todos`, data).then((response) => {
                // log response data to the browser console
                //console.log(response.data);
                
                // assign response to data variable
                this.assignee = response.data;
                //console.log(response.status);
                if(response.status==201){
                    this.$router.push("/toDos")

                }

            }).catch(error => {
                console.log(error.response.data.message )
                console.log(error.message)
                this.showToastMessage(
                    error.message,
                    error.response.data.message,
                    "danger"
                )
            }

            );
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
        //this.help();
        this.fetchAllAssignees();
    }

};
</script>

<style>
.assigneeBox {
    padding: 10px;
    margin: 3px;
    border: 1px solid #42b983;
    float: left;
    min-width: 250px;
    text-align: center;
}

.catBox button {
    margin: 5px;
}
</style>