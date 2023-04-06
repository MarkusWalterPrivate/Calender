<template>
    <div>
        <div style="float:left; width 90%">
        <h1 >Assignees</h1>
        <div>
            <b-alert v-bind:show="assignees.length === 0" variant="warning">
                No Assigneees available on the server...
            </b-alert>
            <!-- what is shown when Assignees are only listed-->
        <div    v-if="editMode==false">
            
            <div  class="assigneeBox" v-for="assignee in assignees" v-bind:key="assignee.id">
                
                <h3>{{assignee.prename}} {{ assignee.name }}</h3>
                E-Mail: {{ assignee.email }}
                <hr style="height:0px;border-width:0;"> 

                    <b-button
                        size="sm"
                        variant="outline-danger"
                        v-on:click="deleteAssignee(assignee.id)"
                        justify= "space around"
                    >
                        <b-icon-trash></b-icon-trash> Delete
                    </b-button> &nbsp;
                    <b-button
                        size="sm"
                        variant="warning"
                        v-on:click="editAssignee(assignee.id)"
                        justify= "space around"
                    >
                        Edit
                    </b-button>
   
            </div>

        </div>
            <!-- what is shown when a Assignee is edited-->
            <div v-if="editMode" class="assigneeBox"  v-bind:key="eAssignee.id">
                <form>
                    <div class="form-group">
                        Prename:
                        <input
                            v-model="eAssignee.prename"
                            class="form-control"
                        />
                    </div>    
                    <div class="form-group">
                        Name:
                        <input
                            v-model="eAssignee.name"
                            class="form-control"
                        />
                    </div>
                    <div class="form-group">
                        Valid @xxxx.uni-stuttgart.de Mai:
                        <input
                            v-model="eAssignee.email"
                            class="form-control"
                        />
                    </div>
                <b-button variant= "warning" v-on:click="saveEdits(eAssignee.id)" >Save Changes</b-button>&nbsp;
                <b-button variant= "outline-danger" v-on:click="discard()" >Discard Changes</b-button>   
                </form>
            </div>
        </div>
        </div>
        <div style="float:right; width 10%">
            <b-button variant="primary" href="/#/createAssignee">New Assignee</b-button>
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
            assignees: [],
            eAssignee:{},
            deleteMessage: "",
            name:"",
            prename:"",
            email:"",
            editMode: false
        };
    },
    methods: {
        // send GET request to API to fetch all cats
        fetchAllAssignees: function () {
            axios.get(`${config.apiBaseUrl}/assignees`).then((response) => {
                // log response data to the browser console
                //console.log(response.data);
                // assign response to data variable
                this.assignees = response.data;
            });
        },
        deleteAssignee: function (id) {
            this.$bvModal.msgBoxConfirm('Please confirm that you want to delete this Assignee', {
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
            axios.delete(`${config.apiBaseUrl}/assignees/${id}`).then(() => {
                // update view by removing deleted cat
                this.assignees = this.assignees.filter((assignee) => assignee.id !== id);
                // show success message using BootstrapVue toast component
                
                this.showToastMessage(
                    "Alert",
                    `Successfully deleted assignee with ID ${id}!`,
                    "success"
                    
                );
            });
            }
            })
          .catch(err => {
            console.log(err)
          })
            
        },
        editAssignee: function(id){
            axios.get(`${config.apiBaseUrl}/assignees/${id}`).then((response) => {
                this.editMode= true
                // update view by removing deleted cat
                // show success message using BootstrapVue toast component
                this.eAssignee = response.data;
                //console.log(response.data);
                //console.log(this.eAssignee)
            });
        },
        saveEdits: function(id){
            axios.put(`${config.apiBaseUrl}/assignees/${id}`, this.eAssignee).then((response) => {
                this.editMode= true
                // update view by removing deleted cat
                // show success message using BootstrapVue toast component
                this.eAssignee = response.data;
                //console.log(response.data);
                if(response.status==200){
                    this.showToastMessage(
                        "Success",
                        `Successfully saved changes to assignee with ID ${response.data.id}!`,
                        "success"
                    )
                    //leave edit mode and flush data
                    this.discard()
                    // update local data by requesting it again from server
                    this.fetchAllAssignees();
                }
            }).catch(error =>{
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
            this.editMode= false
            this.eAssignee={}
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
        this.fetchAllAssignees();
    }
};
</script>

<style>
.assigneeBox {
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
</style>