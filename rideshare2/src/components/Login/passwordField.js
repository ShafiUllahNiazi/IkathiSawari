import React from 'react'
import {Platform, StyleSheet, Text, View,TextInput,TouchableOpacity} from 'react-native';

const styles = StyleSheet.create({
    inputBox:{

        width : 300,
        color:'#000000',
        backgroundColor:'#FFFFFF',
        borderRadius:25,
        paddingHorizontal:16,
        marginVertical: 10,
        fontSize:18,
      },
  });

const Password = props => (
    
   <TextInput placeholder={props.label}
   style= {styles.inputBox}
   secureTextEntry={true}/>
)

export default Password