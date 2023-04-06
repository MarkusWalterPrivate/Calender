<template>
    <div id="app">
        <!-- this navigation will appear on every route -->

        <b-navbar toggleable="lg" type="dark" variant="primary" >
            <b-navbar-brand to="/">
                <img alt="Vue logo" src="./assets/logo.png" class="logo" />
                Yet To-Do
            </b-navbar-brand>

            <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

            <b-collapse id="nav-collapse" is-nav>
                <b-navbar-nav>
                    <b-nav-item-dropdown text="Assignees" right>
                        <b-dropdown-item to="/assignees">Manage All Assignees</b-dropdown-item>
                        <b-dropdown-item to="/createAssignee">New Assignee</b-dropdown-item>
                    </b-nav-item-dropdown>
                    <b-nav-item-dropdown text="To-Dos" right>
                        <b-dropdown-item to="/toDos">Manage All To-Dos</b-dropdown-item>
                        <b-dropdown-item to="/createToDo">New To-Do</b-dropdown-item>
                    </b-nav-item-dropdown>
                </b-navbar-nav>

                <!-- Right aligned nav items -->
                <b-navbar-nav class="ml-auto">
                    <b-nav-item to="/about">About</b-nav-item>
                </b-navbar-nav>
            </b-collapse>
        </b-navbar>

        <!-- the different view templates will appear in this HTML element based on the route -->
        <div class="main">
            <router-view></router-view>
        </div>
    </div>
</template>

<style>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    color: #2c3e50;
}

.logo {
    height: 35px;
    margin: 0 3px;
}

.main {
    padding: 10px 20px;
}
</style>
