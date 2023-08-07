import React, { useLayoutEffect } from "react";
import styled from "styled-components/native";
import Button from "../components/Button";
import { MaterialCommunityIcons } from "@expo/vector-icons";

const Container = styled.View`
  flex: 1;
  align-items: center;
  justify-content: center;
`

const StyledText = styled.Text`
  font-size: 30px;
  margin: 10px;
`

const Chat = ({navigation, route}) => {
  useLayoutEffect(() => {
    navigation.setOptions({
      headerLeft: ({onPress, tintColor}) => {
        return (
          <MaterialCommunityIcons
            name="chevron-left"
            size={30}
            style={{ marginLeft: 11 }}
            color={tintColor}
            onPress={onPress}
          />
        )
      },
      headerRight: ({tintColor}) => {
        return (
          <MaterialCommunityIcons
            name="home"
            size={30}
            style={{ marginRight: 11 }}
            color={tintColor}
            onPress={() => navigation.popToTop()}
          />
        )
      }
    })
  }, [])

  return (
    <Container>
      <StyledText>Chat</StyledText>
      <StyledText>{route.params.id}</StyledText>
      <StyledText>{route.params.name}</StyledText>
      <Button title="Home" onPress={() => navigation.reset({routes: [{name: 'Home'}]})}/>
    </Container>
  )
}

export default Chat