import React from "react";
import styled from "styled-components/native";
import StackNav from "./navigations/Stack";
import {NavigationContainer} from "@react-navigation/native";

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
  return (
    <NavigationContainer>
      <StackNav/>
    </NavigationContainer>
  );
}