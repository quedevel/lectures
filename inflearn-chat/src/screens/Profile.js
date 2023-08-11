import React, {useContext, useState} from "react";
import {UserContext} from "../contexts";
import styled, {ThemeContext} from "styled-components/native";
import {Button, Input, Image} from "../components";
import {getCurrentUser, signout, updateUserInfo} from "../firebase";
import {Alert} from "react-native";
import {ProgressContext} from "../contexts";

const Container = styled.View`
  flex: 1;
  background-color: ${({theme}) => theme.background};
  justify-content: center;
  align-items: center;
  padding: 0 20px;
`

const Profile = ({navigation, route}) => {
  const {setUser} = useContext(UserContext)
  const theme = useContext(ThemeContext)
  const {spinner} = useContext(ProgressContext)
  const user = getCurrentUser()

  const [photo, setPhoto] = useState(user.photo)

  const _handlePhotoChange = async url => {
    try {
      spinner.start()
      const photoURL = await updateUserInfo(url)
      setPhoto(photoURL)
    } catch (e) {
      Alert.alert('Photo Error', e.message)
    } finally {
      spinner.stop()
    }
  }

  return (
    <Container>
      <Image showButton url={photo} onChangePhoto={_handlePhotoChange}/>
      <Input label="Name" value={user.name} disabled/>
      <Input label="Email" value={user.email} disabled/>
      <Button
        onPress={async () => {
          try {
            spinner.start()
            await signout()
          } catch (e) {
          } finally {
            setUser({})
            spinner.stop()
          }
        }}
        title="signout"
        containerStyle={{
          backgroundColor: theme.btnSignout
        }}
      />
    </Container>
  )
}

export default Profile