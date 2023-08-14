import React, {useState, useEffect} from "react";
import {FlatList} from "react-native";
import styled from "styled-components/native";
import {MaterialIcons} from "@expo/vector-icons";
import {db} from "../firebase";
import {collection, query, orderBy, getDocs} from "firebase/firestore"
import moment from "moment";

const getDateOrTime = ts => {
  const now = moment().startOf('day')
  const target = moment(ts).startOf('day')
  return moment(ts).format(now.diff(target, 'day') > 0 ? 'MM/DD' : 'HH:mm')
}

const ItemContainer = styled.TouchableOpacity`
  flex-direction: row;
  align-items: center;
  border-bottom-width: 1px;
  border-color: ${({theme}) => theme.itemBorder};
  padding: 15px 20px;
`

const ItemTextContainer = styled.View`
  flex: 1;
  flex-direction: column;
`

const ItemTitle = styled.Text`
  font-size: 20px;
  font-weight: 600;
`

const ItemDesc = styled.Text`
  font-size: 16px;
  margin-top: 5px;
  color: ${({theme}) => theme.itemDesc};
`
const ItemTime = styled.Text`
  font-size: 12px;
  color: ${({theme}) => theme.itemTime};
`
const ItemIcon = styled(MaterialIcons).attrs(({theme}) => ({
  name: 'keyboard-arrow-right',
  size: 24,
  color: theme.itemIcon
}))``

const Item = ({item: {title, description, createdAt}, onPress}) => {
  return (
    <ItemContainer onPress={() => onPress({title, description})}>
      <ItemTextContainer>
        <ItemTitle>{title}</ItemTitle>
        <ItemDesc>{description}</ItemDesc>
      </ItemTextContainer>
      <ItemTime>{getDateOrTime(createdAt)}</ItemTime>
      <ItemIcon/>
    </ItemContainer>
  )
}

const Container = styled.View`
  flex: 1;
  background-color: ${({theme}) => theme.background};
`

const StyledText = styled.Text`
  font-size: 30px;
`

const ChannelList = ({navigation}) => {
  const [channels, setChannels] = useState([])

  useEffect(() => {
    const q = query(collection(db, 'channels'), orderBy('createdAt', 'desc'))
    getDocs(q).then((querySnapshot) => {
      const list = []
      querySnapshot.forEach(doc => {
        list.push(doc.data())
      })
      setChannels(list)
    })
  }, [])

  return (
    <Container>
      <FlatList
        data={channels}
        renderItem={({item}) =>
          <Item
            item={item}
            onPress={params => navigation.navigate('Channel', params)}
          />
        }
        keyExtractor={item => item.createdAt}
      />
    </Container>
  )
}

export default ChannelList