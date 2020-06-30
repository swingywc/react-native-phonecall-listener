> ⚠️ NOTE: This package only support Android.
> Feel free to contribute for iOS side.
# React Native Phonecall Listener
This package is for listening to any phonecall received, and get the phone number for further handling.

### Support Versions
- React Native 0.59 or above.

## Installation

`$ npm install react-native-phonecall-listener --save`

### Linking For `react-native 0.59`
> If you are using React Native 0.60 or above, please skip this command as React  Native has autolink function.

`$ react-native link react-native-phonecall-listener`

### Add Android Permissions and Receiver

Open `android/app/src/main/AndroidManifest.xml`:
- Add permissions:
```java
<uses-permission android:name="android.permission.READ_CALL_LOG" />
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
<uses-permission android:name="android.permission.CALL_PHONE" />
```
- Add receiver at the end inside of `<application/>` tag:
```java
<receiver android:name="com.reactnative.phonecalllistener.PhoneStateReceiver">
    <intent-filter>
        <action android:name="android.intent.action.PHONE_STATE" />
    </intent-filter>
</receiver>
```

## Functions
| Name | Parameters | Description |
|--------|---------------|----------|
| enable() | `null` | Run this function on the component before you receive and record phonecall |
| disable() | `null` | Run this function on unmount function of component after you finished recording |
| onCallReceived() | `function` | Phone number will be passed as string into arrow function when received phonecall |


## Usage
Please use with react native `PermissionsAndroid` to get permission from users.

```javascript
import React, { Component } from 'react';
import { Platform, PermissionsAndroid } from 'react-native';
import PhonecallListener from 'react-native-phonecall-listener';

class ReceivePhonecallScreen extends Component {
  phonecallListener;

  async componentDidMount() {
    await this.listenPhoneCall();
  }

  componentWillUnmount() {
    this.phonecallListener && this.phonecallListener.remove();
    PhonecallListener.disable();
  }

  listenPhoneCall = async () => {
    if (Platform.OS === 'android') {
      const isCallLogGranted = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.READ_CALL_LOG) === PermissionsAndroid.RESULTS.GRANTED;
      const isPhoneStateGranted = await PermissionsAndroid.request(PermissionsAndroid.PERMISSIONS.READ_PHONE_STATE) === PermissionsAndroid.RESULTS.GRANTED;
      if (isCallLogGranted && isPhoneStateGranted) {
        await PhonecallListener.enable();
        this.phonecallListener = PhonecallListener.onCallReceived(this.handlePhoneNumber);
      } else {
        Alert.alert("Permission denied from user for read phone state");
      }
    }
  };

  handlePhoneNumber = (phoneNumber) => {
    // handle phoneNumber here...
  };

  ...
  render() {...}
}
```

## Troubleshooting

#### `PhonecallListener.onCallReceived` cannot detect the phonecall in Android 9 or above devices
If you try to print the `incomingNumber` in the Android native side, you should find that the value is `null`.
It is because in Android 9 or above version, applications need to request both `PERMISSIONS.READ_CALL_LOG` and `PERMISSIONS.READ_PHONE_STATE` in live. 
Missing either one of the permissions will be failed to get the phone number. You can have a look on the sample codes above to learn how to request Android permissions in `react-native`.

## TO-DO

- [ ] Add iOS side feature
- [ ] Add unit test
