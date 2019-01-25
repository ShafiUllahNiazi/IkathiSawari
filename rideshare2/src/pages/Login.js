/**
 * Login React Native App
 * 
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View,TouchableWithoutFeedback,TouchableOpacity} from 'react-native';
import {Actions} from 'react-native-router-flux'
import Logo from '../components/Login/Logo'
import Input from '../components/Login/inputField'
import Password from '../components/Login/passwordField'
import MyButton from '../components/Login/MyButton'





type Props = {};
export default class Login extends Component<Props> {

  signup(){
    Actions.signup()
  }


  render() {
    return (
      <View style={styles.container}>


        <Logo/>
        <Input label ={'email'}/>
        <Password label ={'pasword'}/>
        <MyButton btnLabel = {'Login'}/>
        <View style={styles.link}>

          <Text style={styles.componentText}>Don't have account? </Text>
          <TouchableWithoutFeedback
          onPress={this.signup}
           >
              <View >
                  <Text style={styles.componentText}>Sign up</Text>
              </View>
          </TouchableWithoutFeedback>
        </View>
        

      {/* <Logo></Logo>
      <LoginForm></LoginForm>
      <SignUpLink></SignUpLink> */}

      
      </View>
    );
  }
}

const styles = StyleSheet.create({
  
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#455a64',
  },
  componentText:{
    fontSize:14,
    color : '#ffffff',
    textAlign:'center',

  },
  link:{
      flexDirection:'row',
      
  },
  LoginLabels: {
    color:'#ffffff',
    fontSize: 30,
  },
  inputBox:{

    width : 300,
    color:'#ffffff',
    backgroundColor:'rgba(255,255,255,0.3)',
    borderRadius:25,
    paddingHorizontal:16,
    marginVertical: 10,
    fontSize:18,
  },
  button:{
    width : 300,
    // color:'#ffffff',
    backgroundColor:'#1c313a',
    borderRadius:25,
    paddingVertical:10,
    marginVertical: 10,
  },
  buttonText:{
    textAlign:'center',
    

    // width : 300,
    color:'#ffffff',
    fontWeight:'500',
    fontSize:18,
    // backgroundColor:'rgba(255,255,255,0.3)',
    // borderRadius:25,
    // paddingHorizontal:16,
    // marginVertical: 10,
  },

  
});
