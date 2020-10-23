#include <Arduino_LSM9DS1.h>
#include <ArduinoBLE.h>

//통신 방식 설정 (uuid 설정등)
BLEService accelscopeService("0000fff0-0000-1000-8000-00805f9b34fb");       
BLECharacteristic accelscopeChar("00002902-0000-1000-8000-00805f9b34fb",BLERead | BLENotify, 32, (1==1));

long previousMillis = 0;
char buf[32];

const double RADIAN_TO_DEGREE = 180 / 3.14159;

void setup() {
  Serial.begin(9600);    
  //while (!Serial);

  pinMode(LED_BUILTIN, OUTPUT); 

  // begin initialization
  if (!BLE.begin()) {
    Serial.println("starting BLE failed!");
    while (1);
  }

  // IMU센서를 초기화합니다. 초기화중 문제가 발생하면 오류를 발생시킵니다.
  if (!IMU.begin()) {
    Serial.println("Failed to initialize IMU!");
    while (1);
  }

  BLE.setLocalName("Upright");  
  BLE.setAdvertisedService(accelscopeService);
  accelscopeService.addCharacteristic(accelscopeChar); 
  BLE.addService(accelscopeService); 
  accelscopeChar.writeValue("oldxyz"); 
  BLE.advertise();

  Serial.println(accelscopeService.uuid());
  Serial.println("Bluetooth device active, waiting for connections...");
}



// 각 센서별로 XYZ값을 저장할 변수
float accel_x, accel_y, accel_z;
int timePeriod = 1000;       //업데이트 주기 (ms단위)
double angelAcX,angelAcY,angelAcZ;
void loop() {
  BLEDevice central = BLE.central();

  if (central) {
    Serial.print("Connected to central: ");
    Serial.println(central.address());
   
    while (central.connected()) {
      long currentMillis = millis();
      //timePeriod 초마다 자이로 센서값 업데이
      if (currentMillis - previousMillis >= timePeriod) {
        previousMillis = currentMillis;
        updateAccelscope();
      }
    }
    
    Serial.print("Disconnected from central: ");
    Serial.println(central.address());
  }
}

void updateAccelscope() {
  if (IMU.accelerationAvailable()) {
    IMU.readAcceleration(accel_x, accel_y, accel_z);
    
    angelAcY = atan(-accel_x / sqrt(pow(accel_y,2) + pow(accel_z,2)));
    angelAcY *= RADIAN_TO_DEGREE;
    angelAcX = atan(accel_y / sqrt(pow(accel_x, 2) + pow(accel_z,2)));
    angelAcX *= RADIAN_TO_DEGREE;

    angelAcZ = atan(accel_z / sqrt(pow(accel_x,2) + pow(accel_y,2)));
    angelAcZ *= RADIAN_TO_DEGREE;

    
    String accelPacket = String(angelAcX) + "," + String(angelAcY) + "," + String(angelAcZ);
    accelPacket.toCharArray(buf,32);
    Serial.println(accelPacket);

  // 문자열을 블루투스 신호로 보냅니다.
    accelscopeChar.writeValue(buf, 32);
  }
}
