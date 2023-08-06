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

const items = [
  {id: 1, name: 'React Native'},
  {id: 2, name: 'React '},
  {id: 3, name: ' Native'},
]

const List = () => {
  return (
    <Container>
      <StyledText>List</StyledText>
      {items.map(({id, name}) =>
        <Button key={id} title={name} />
      )}
    </Container>
  )
}

export default List