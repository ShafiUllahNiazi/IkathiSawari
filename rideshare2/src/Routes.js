import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';
import {Router,Stack,Scene} from 'react-native-router-flux'
import Login from './pages/Login'
import Signup from './pages/Signup'

const Routes = () => (
    <Router>
      <Stack key="root" hideNavBar={true}>
        <Scene key="login" component={Login} title="Login" initial={true}/>
        <Scene key="signup" component={Signup} title="Register"/>
      </Stack>
    </Router>
    
  );
  
  export default Routes;