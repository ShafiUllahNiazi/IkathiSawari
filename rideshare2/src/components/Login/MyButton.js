import React from 'react'
import {Platform, Button, StyleSheet, Text, View,TextInput,TouchableOpacity} from 'react-native';

const styles = StyleSheet.create({
    
      button:{
        width : 300,
        // color:'#ffffff',
        backgroundColor:'#102027',
        borderRadius:25,
        paddingVertical:10,
        marginVertical: 10,
      },
      buttonText:{
        fontSize:18,
        color : '#ffffff',
        textAlign:'center',
      }
  });

const MyButton = props => (
  <View>
    <TouchableOpacity style={styles.button}>
        <Text style={styles.buttonText} >{props.btnLabel}</Text>
      </TouchableOpacity>
  </View>
    
)

export default MyButton