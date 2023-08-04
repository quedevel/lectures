import React from "react";
import {TouchableOpacity, View, Text} from "react-native";
import PropTypes from "prop-types";

const MyButton = ({title, onPress, children}) => {
  return (
    <TouchableOpacity
      onPress={onPress}
      // hitSlop={{bottom: 100, top: 100, left: 100, right: 100}}
      // pressRetentionOffset={{bottom: 100, top: 10, left: 10, right: 10}}
      // onPressIn={() => console.log(('in'))}
      // onPress={() => console.log(('press'))}
      // onPressOut={() => console.log(('out'))}
      // onLongPress={() => console.log(('long'))}
      // delayLongPress={3000}
    >
      <View style={{backgroundColor: 'red', padding: 10, margin: 10}}>
        <Text style={{fontSize: 20, color: 'white'}}>{children || title}</Text>
      </View>
    </TouchableOpacity>
  )
}

MyButton.defaultProps = {
  title: 'default',
  onPress: () => alert('default!')
}

MyButton.prototype = {
  title: PropTypes.string.required,
  onPress: PropTypes.func
}

export default MyButton