import React from "react";
import {createStackNavigator} from "@react-navigation/stack";
import {Home, List, Chat} from "../screens"
import {MaterialCommunityIcons} from "@expo/vector-icons";

const Stack = createStackNavigator();

const StackNav = () => {
  return (
    <Stack.Navigator
      initialRouteName="Home"
      screenOptions={{
        cardStyle: {backgroundColor: '#ffffff'},
        headerStyle: {
          height: 120,
          backgroundColor: '#555555',
          borderBottomWidth: 5,
          borderBottomColor: '#111111',
        },
        headerTitleStyle: {
          fontSize: 24,
          color: '#ffffff'
        },
        headerTitleAlign: 'center',
        headerTitle: ({style, tintColor}) => {
          return (
            <MaterialCommunityIcons
              name="react"
              style={style}
              color={tintColor}
            />)
        },
        headerTintColor: '#ff6600',
        headerBackTitle: 'Prev',
        headerBackTitleVisible: false,
        headerBackTitleStyle: {
          fontSize: 26
        },
      }}
    >
      <Stack.Screen
        name="Home"
        component={Home}
        // options={{headerShown: false}}
      />
      <Stack.Screen name="List" component={List}/>
      <Stack.Screen
        name="Chat"
        component={Chat}
        options={{
          headerTitle: 'Chat Screen',
          headerBackImage: ({tintColor}) => {
            return (
              <MaterialCommunityIcons
                name="chevron-left"
                size={26}
                color={tintColor}
              />
            )
          }
        }}
      />
    </Stack.Navigator>
  )
}

export default StackNav