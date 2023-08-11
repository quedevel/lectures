import React from "react";
import styled from "styled-components/native";
import {Button} from "../components";

const Container = styled.View`
  flex: 1;
  background-color: ${({theme})=> theme.background};
`

const StyledText = styled.Text`
  font-size: 30px;
`

const Channel = ({route}) => {
  console.log('route', route)
  return (
    <Container>
      <StyledText>Channel</StyledText>
    </Container>
  )
}

export default Channel