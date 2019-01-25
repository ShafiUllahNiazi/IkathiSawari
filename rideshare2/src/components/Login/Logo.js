/**
 * Logo React Native App
 * 
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';


type Props = {};
export default class Logo extends Component<Props> {
  render() {
    return (
      <View style = {styles.container}>
      {/* <Image style={{width:70,height:40}} source={require('../imgs/logo.png')}></Image> */}
      <Text style={styles.LogoLabels}>Ikathi Sawari</Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    justifyContent: 'center',
    alignItems: 'center',
    marginVertical: 30,
    
  },
  LogoLabels: {
    color:'#ffffff',
    fontSize: 30
  }
  
});