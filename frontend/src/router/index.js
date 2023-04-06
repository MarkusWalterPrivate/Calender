import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";
import About from "../views/About.vue";
import Assignees from "../views/Assignees.vue";
import CreateAssignee from "../views/CreateAssignee.vue";
import ToDos from "../views/ToDos.vue";
import CreateToDo from "../views/CreateToDo.vue";

Vue.use(VueRouter);

// all frontend routes of the app, i.e. bind a path to a Vue component
const routes = [
    {
        path: "/",
        name: "Home",
        component: Home
    },
    {
        path: "/about",
        name: "About",
        component: About
    },
     {
        path: '/assignees', 
        name: 'Assignees', 
        component: Assignees
     },
     {
        path: '/createAssignee', 
        name: 'CreateAssignee', 
        component: CreateAssignee
     },
     {
        path: '/toDos', 
        name: 'ToDos', 
        component: ToDos
     },
     {
        path: '/createToDo', 
        name: 'CreateToDo', 
        component: CreateToDo
     }
];

const router = new VueRouter({
    routes
});

export default router;
