import React, {useEffect, useState, useLayoutEffect} from "react";
import styled from "styled-components/native";
import {createdMessage, db, getCurrentUser} from "../firebase";
import {GiftedChat, Send} from "react-native-gifted-chat";
import {Alert} from "react-native";
import {collection, doc, orderBy, query, onSnapshot} from "firebase/firestore";
import {MaterialIcons} from "@expo/vector-icons";
import navigations from "../navigations";

const Container = styled.View`
  flex: 1;
  background-color: ${({theme}) => theme.background};
`
const SendIcon = styled(MaterialIcons).attrs(({theme, text}) => ({
  name: 'send',
  size: 24,
  color: text ? theme.sendBtnActive : theme.sendBtnInactive
}))``;

const SendButton = props => {
  return (
    <Send {...props}
          containerStyle={{
            width: 44,
            height: 44,
            alignItems: 'center',
            justifyContent: 'center',
            marginHorizontal: 4,
          }}
          disabled={!props.text}
    >
      <SendIcon text={props.text} name="send" size={24}/>
    </Send>
  )
}

const Channel = ({navigation, route}) => {
  const [messages, setMessages] = useState([])
  const {uid, name, photo} = getCurrentUser()

  useLayoutEffect(() => {
    navigation.setOptions({
      headerTitle: route.params.title || 'Channel'
    })
  }, [])

  useEffect(() => {
    const channelsRef = doc(db, 'channels', route.params.title);
    const messagesRef = collection(channelsRef, 'messages');
    const q = query(messagesRef, orderBy('createdAt', 'desc'));

    const unsubscribe = onSnapshot(q, (querySnapshot) => {
      const list = [];
      querySnapshot.forEach((doc) => {
        list.push({...doc.data(), _id: doc.id}); // Include document ID as _id for GiftedChat
      });
      setMessages(list);
    });

    return () => {
      unsubscribe(); // Unsubscribe when component unmounts
    };
  }, [route.params.title]);

  const _handleMessageSend = async messageList => {
    const message = messageList[0];
    try {
      await createdMessage({
        channelId: route.params.title,
        message
      })
    } catch (e) {
      Alert.alert('Message Error', e.message)
    }
  }

  return (
    <Container>
      <GiftedChat
        placeholder="Enter a message ..."
        messages={messages}
        user={{
          _id: uid, name, avatar: photo
        }}
        onSend={_handleMessageSend}
        scrollToBottom={true}
        alwaysShowSend={true}
        renderSend={props => <SendButton {...props}/>}
      />
    </Container>
  )
}

export default Channel