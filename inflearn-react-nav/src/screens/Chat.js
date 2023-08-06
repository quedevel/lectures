import React from "react";
import styled from "styled-components/native";
import Button from "../components/Button";

const Container = styled.View`
  flex: 1;
  align-items: center;
  justify-content: center;
`

const StyledText = styled.Text`
  font-size: 30px;
  margin: 10px;
`

const Chat = () => {
  return (
    <Container>
      <StyledText>Chat</StyledText>
      <Button title="Home" />
    </Container>
  )
}

export default Chat