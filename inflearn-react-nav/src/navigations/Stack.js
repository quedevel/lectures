import React from "react";
import {createStackNavigator} from "@react-navigation/stack";
import {Home, List, Chat} from "../screens"

const Stack = createStackNavigator();

const StackNav = () => {
  return (
    <Stack.Navigator>
      <Stack.Screen name="Home" component={Home} />
      <Stack.Screen name="List" component={List} />
      <Stack.Screen name="Chat" component={Chat} />
    </Stack.Navigator>
  )
}

export default StackNav