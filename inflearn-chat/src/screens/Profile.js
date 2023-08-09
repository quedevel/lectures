import React from "react";
import styled from "styled-components/native";
import {Button} from "../components";

const Container = styled.View`
  flex: 1;
  background-color: ${({theme}) => theme.background};
`

const Profile = ({navigation, route}) => {
  return (
    <Container>
      <Button onPress={()=> navigation.navigate('SignUp')} title="signout" />
    </Container>
  )
}

export default Profile