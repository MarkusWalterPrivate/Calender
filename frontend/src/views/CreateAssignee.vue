<template>
    <div>
        <h1>Create a new Assignees</h1>
        <div>
            <form>
              <div class="form-group">
                  Prename:
                <input
                  v-model="prename"
                  placeholder="Prename"
                  class="form-control"
                />
              </div>    
              <div class="form-group">
                Name: 
                <input
                v-model="name"
                placeholder="Last Name"
                class="form-control"
              />
              </div>
              <div class="form-group">
                  E-Mail: 
                <input
                v-model="email"
                placeholder="Valid @xxxx.uni-stuttgart.de Mail"
                class="form-control"
              />
            </div>
            <b-button variant= "primary" v-on:click="createAssignees()" >Create Assignee</b-button>  
            </form>
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
    data(){
        return {
            assignee: {},
            prename:"",
            name:"",
            email: "",
            deleteMessage: ""
        };
    },
    methods: {
        // send GET request to API to fetch all cats
        createAssignees: function () {
            const data = {'prename': this.prename, 'name': this.name, 'email': this.email}
            axios.post(`${config.apiBaseUrl}/assignees`, data).then((response) => {
                // log response data to the browser console
                //console.log(response.data);
                
                // assign response to data variable
                this.assignee = response.data;
                //console.log(response.status);
                if(response.status==201){
                    this.$router.push('/Assignees')
                    

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