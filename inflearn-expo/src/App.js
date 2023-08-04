import {StyleSheet, Text, View, Button, TextInput} from 'react-native';
import MyButton from "./MyButton";
import React, {useState} from "react";
import {styles} from "./style";
import Box from "./Box";
import styled from "styled-components/native";

const Container = styled.View`
  flex: 1;
  background-color: #e3e3e3;
  align-items: center;
  justify-content: center;
`

const StyleText = styled.Text`
  font-size: 20px;
  font-weight: 600;
  color: blue;
`

export default function App() {
  // const [addition, setAddition] = useState(0)
  // const [multiple, setMultiple] = useState(1)


  return (
    <Container>
      <StyleText>Hi</StyleText>
      {/*<Box style={{backgroundColor: 'red', flex: 2}}/>*/}
      {/*<Box style={{backgroundColor: 'green', flex: 10}}/>*/}
      {/*<Box style={{backgroundColor: 'blue', flex: 3}}/>*/}
      {/*<Text style={styles.errorText}>*/}
      {/*  Error!!*/}
      {/*</Text>*/}
      {/*<Text style={styles.text}>*/}
      {/*  Open up App.js to start working on your app!*/}
      {/*</Text>*/}
      {/*<TextInput*/}
      {/*  onChange={event => console.log(event.nativeEvent)}*/}
      {/*  style={{borderWidth: 1, padding: 10, fontSize: 20}}/>*/}
      {/*<MyButton title="my button1 title" onPress={() => alert('1')}/>*/}
      {/*<MyButton title="my button2 title" onPress={() => alert('2')}/>*/}
      {/*<MyButton title="my button3 title" onPress={() => alert('3')}>Children</MyButton>*/}
      {/*<MyButton/>*/}

      {/*<Text style={{fontSize: 20}}>{addition}</Text>*/}
      {/*<Text style={{fontSize: 20}}>{multiple}</Text>*/}
      {/*<MyButton title="addition" onPress={() => setAddition(addition+2)} />*/}
      {/*<MyButton title="multiple" onPress={() => setMultiple(multiple*2)} />*/}
    </Container>
  );
}