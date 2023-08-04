import {StyleSheet, Text, View, Button, TextInput} from 'react-native';
import MyButton from "./MyButton";
import React, {useState} from "react";

export default function App() {
  const [addition, setAddition] = useState(0)
  const [multiple, setMultiple] = useState(1)


  return (
    <View style={styles.container}>
      <TextInput
        onChange={event => console.log(event.nativeEvent)}
        style={{borderWidth: 1, padding: 10, fontSize: 20}}/>
      {/*<MyButton title="my button1 title" onPress={() => alert('1')}/>*/}
      {/*<MyButton title="my button2 title" onPress={() => alert('2')}/>*/}
      {/*<MyButton title="my button3 title" onPress={() => alert('3')}>Children</MyButton>*/}
      {/*<MyButton/>*/}

      {/*<Text style={{fontSize: 20}}>{addition}</Text>*/}
      {/*<Text style={{fontSize: 20}}>{multiple}</Text>*/}
      {/*<MyButton title="addition" onPress={() => setAddition(addition+2)} />*/}
      {/*<MyButton title="multiple" onPress={() => setMultiple(multiple*2)} />*/}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#fff',
    alignItems: 'center',
    justifyContent: 'center',
  },
});
