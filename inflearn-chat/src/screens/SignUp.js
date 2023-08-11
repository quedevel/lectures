import React, {useState, useRef, useEffect, useContext} from "react";
import styled from "styled-components/native";
import {Button, Image, Input, ErrorMessage} from '../components'
import {useSafeAreaInsets} from "react-native-safe-area-context";
import {KeyboardAwareScrollView} from "react-native-keyboard-aware-scroll-view";
import {signUp} from "../firebase";
import {Alert} from "react-native";
import {validateEmail, removeWhitespace} from "../utils";
import {UserContext, ProgressContext} from "../contexts";

const Container = styled.View`
  flex: 1;
  justify-content: center;
  align-items: center;
  background-color: ${({theme}) => theme.background};
  padding: 50px 20px;
`

const DEFAULT_IMAGE = 'https://firebasestorage.googleapis.com/v0/b/inflearn-chat-221c9.appspot.com/o/face.png?alt=media';


const SignUp = ({navigation}) => {
  const insets = useSafeAreaInsets()

  const {setUser} = useContext(UserContext)
  const {spinner} = useContext(ProgressContext)

  const [photo, setPhoto] = useState(DEFAULT_IMAGE)
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [passwordConfirm, setPasswordConfirm] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const [disabled, setDisabled] = useState(true);

  const refEmail = useRef(null)
  const refPassword = useRef(null)
  const refPasswordConfirm = useRef(null)
  const refDidMount = useRef(null)

  useEffect(() => {
    setDisabled(!(name && email && password && passwordConfirm && !errorMessage))
  }, [email, name, password, passwordConfirm, errorMessage])

  useEffect(() => {
    if (refDidMount.current){
      let error = ''
      if (!name) {
        error = 'Please enter your name'
      } else if (!email) {
        error = 'Please enter your email'
      } else if (!validateEmail(email)) {
        error = 'Please verify your email'
      }  else if (password.length < 6) {
        error = 'The password must contain 6 characters at least'
      } else if (password !== passwordConfirm) {
        error = 'Password need to match'
      }
      setErrorMessage(error)
    } else {
      refDidMount.current = true
    }
  }, [email, name, password, passwordConfirm])

  const _handleSingInBtnPress = async () => {
    try {
      spinner.start()
      const {user} = await signUp({name, email, password, photo})
      setUser(user)
    } catch (e) {
      Alert.alert('Sign up error', e.message)
    } finally {
      spinner.stop()
    }
  }

  return (
    <KeyboardAwareScrollView extraScrollHeight={20}>
      <Container insets={insets}>
        <Image showButton={true} url={photo} onChangePhoto={setPhoto}/>
        <Input
          label="Name"
          placeHolder="Name"
          returnKeyType="next"
          value={name}
          onChangeText={setName}
          onSubmitEditing={() => refEmail.current.focus()}
          maxLength={12}
        />
        <Input
          ref={refEmail}
          label="Email"
          placeHolder="Email"
          returnKeyType="next"
          value={email}
          onChangeText={setEmail}
          onSubmitEditing={() => refPassword.current.focus()}
          onBlur={() => setEmail(removeWhitespace(email))}
        />
        <Input
          ref={refPassword}
          label="Password"
          placeHolder="Password"
          returnKeyType="next"
          value={password}
          onChangeText={setPassword}
          isPassword={true}
          onSubmitEditing={() => refPasswordConfirm.current.focus()}
          onBlur={() => setPassword(removeWhitespace(password))}
        />
        <Input
          ref={refPasswordConfirm}
          label="Password Confirm"
          placeHolder="Password"
          returnKeyType="done"
          value={passwordConfirm}
          onChangeText={setPasswordConfirm}
          isPassword={true}
          onSubmitEditing={_handleSingInBtnPress}
          onBlur={() => setPasswordConfirm(removeWhitespace(passwordConfirm))}
        />
        <ErrorMessage message={errorMessage}/>
        <Button
          title="sign up"
          onPress={_handleSingInBtnPress}
          disabled={disabled}
        />
      </Container>
    </KeyboardAwareScrollView>
  )
}

export default SignUp