import {StatusBar, useWindowDimensions} from 'react-native';
import React, {useState, useEffect} from "react";
import styled, {ThemeProvider} from "styled-components/native";
import {theme} from "./theme";
import Input from "./components/Input";
import Task from "./components/Task";
import AsyncStorage from "@react-native-async-storage/async-storage";

const Container = styled.SafeAreaView`
  flex: 1;
  background-color: ${({theme}) => theme.background};
  align-items: center;
  justify-content: flex-start;
`

const Title = styled.Text`
  font-size: 40px;
  font-weight: 600;
  color: ${({theme}) => theme.main};
  width: 100%;
  align-items: flex-end;
  padding: 0 20px;
`

const List = styled.ScrollView`
  flex: 1;
  width: ${({width}) => width - 40}px;
`

export default function App() {
  const width = useWindowDimensions().width

  const [tasks, setTasks] = useState({});
  const [newTask, setNewTask] = useState('')

  const storeData = async tasks => {
    try {
      await AsyncStorage.setItem('tasks', JSON.stringify(tasks))
      setTasks(tasks)
    } catch (e) {
      // ignored
    }
  }

  const getData = async () => {
    try {
      const loadedData = await AsyncStorage.getItem('tasks');
      setTasks(JSON.parse(loadedData || '{}'))
    } catch (e) {
      // ignored
    }
  }

  const addTask = () => {
    if (newTask.length <= 1) {
      return
    }
    const ID = Date.now().toString();
    const newTaskObject = {
      [ID]: {id: ID, text: newTask, completed: false}
    }
    setNewTask('')
    storeData({...tasks, ...newTaskObject})
  }

  const deleteTask = (id) => {
    const currentTasks = Object.assign({}, tasks);
    delete currentTasks[id];
    storeData(currentTasks)
  }

  const toggleTask = id => {
    const currentTasks = Object.assign({}, tasks)
    currentTasks[id]['completed'] = !currentTasks[id]['completed']
    storeData(currentTasks)
  }

  const updateTask = item => {
    const currentTasks = Object.assign({}, tasks)
    currentTasks[item.id] = item;
    storeData(currentTasks)
  }

  const [appIsReady, setAppIsReady] = useState(false);

  useEffect(() => {
    async function prepare() {
      try {
        // Pre-load fonts, make any API calls you need to do here
        await getData()
        // Artificially delay for two seconds to simulate a slow loading
        // experience. Please remove this if you copy and paste the code!
        await new Promise(resolve => setTimeout(resolve, 2000));
      } catch (e) {
        console.warn(e);
      } finally {
        // Tell the application to render
        setAppIsReady(true);
      }
    }
    prepare();
  }, []);

  return (
    <ThemeProvider theme={theme}>
      <Container>
        <StatusBar
          barStyle="light-content"
          backgroundColor={theme.background}
        />
        <Title>TODO List</Title>
        <Input
          placeholder="+ Add a Task"
          value={newTask}
          onChangeText={text => setNewTask(text)}
          onSubmitEditing={addTask}
          onBlur={() => setNewTask('')}
        />
        <List width={width}>
          {Object.values(tasks)
            .reverse()
            .map(item => (
              <Task
                key={item.id}
                item={item}
                deleteTask={deleteTask}
                toggleTask={toggleTask}
                updateTask={updateTask}
              />
            ))
          }
        </List>
      </Container>
    </ThemeProvider>
  )
}
