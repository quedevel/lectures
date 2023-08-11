import React, {useState, useRef, useEffect, useContext} from "react";
import styled from "styled-components/native";
import {Button, Input, ErrorMessage} from "../components";
import {KeyboardAwareScrollView} from "react-native-keyboard-aware-scroll-view";
import {ProgressContext} from "../contexts";
import {createChannel} from "../firebase";
import {Alert} from "react-native";

const Container = styled.View`
  flex: 1;
  background-color: ${({theme}) => theme.background};
  justify-content: center;
  align-items: center;
  padding: 0 20px;
`

const StyledText = styled.Text`
  font-size: 30px;
`

const ChannelCreation = ({navigation}) => {
  const {spinner} = useContext(ProgressContext)

  const [title, setTitle] = useState('')
  const [desc, setDesc] = useState('')
  const [errorMessage, setErrorMessage] = useState('')
  const [disabled, setDisabled] = useState(true)

  const refDesc = useRef(null)

  useEffect(() => {
    setDisabled(!(title && !errorMessage))
  }, [title, errorMessage]);

  const _handleTitleChange = title => {
    setTitle(title)
    setErrorMessage(title.trim() ? '' : 'Please enter the title')
  }
  const _handleDescChange = desc => {
    setDesc(desc)
    setErrorMessage(desc.trim() ? '' : 'Please enter the desc')
  }
  const _handleCreateBtnPress = () => {
    try {
      spinner.start()
      const id = createChannel({title: title.trim(), desc: desc.trim()})
      navigation.replace('Channel', {id, title})
    } catch (e) {
      Alert.alert('error', e.message())
    } finally {
      spinner.stop()
    }
  }

  return (
    <KeyboardAwareScrollView
      contentContainerStyle={{flex: 1}}
      extraScrollHeight={20}
    >
      <Container>
        <Input label="Title" value={title}
               onChangeText={_handleTitleChange}
               onSubmitEditing={() => refDesc.current.focus()}
               placeHolder="Title"
               returnKeyType="next"
               maxLength={20}
        />
        <Input label="Description" value={desc} ref={refDesc}
               onChangeText={_handleDescChange}
               onSubmitEditing={_handleCreateBtnPress}
               placeHolder="Description"
               returnKeyType="done"
               maxLength={40}
        />
        <ErrorMessage message={errorMessage}/>
        <Button
          title="Create"
          onPress={_handleCreateBtnPress}
          disabled={disabled}
        />
      </Container>
    </KeyboardAwareScrollView>
  )
}

export default ChannelCreation