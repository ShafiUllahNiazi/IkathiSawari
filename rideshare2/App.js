/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {Platform, StyleSheet, Text, View} from 'react-native';


import Login from './src/pages/Login'
import Signup from './src/pages/Signup'




import Routes from './src/Routes';

// const App = () => (
//   <Router>
//     <Stack key="root">
//       <Scene key="login" component={Login} title="Login" initial={true}/>
//       <Scene key="signup" component={Signup} title="Register"/>
//     </Stack>
//   </Router>
  
// );

// export default App;

type Props = {};
export default class App extends Component<Props> {


  


  render() {
    return (
      <Routes/>
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
});