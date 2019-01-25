import React from 'react'
import {Platform, Button, StyleSheet, Text, View,TextInput,TouchableOpacity} from 'react-native';
import {StackNavigator} from 'react-navigation'
import Login from '../pages/Login'
import Signup from '../pages/Signup'

const Navigation = () =>StackNavigator({
    First : {screen: Login},
    Second : {screen: Signup},
  })

export default Navigation