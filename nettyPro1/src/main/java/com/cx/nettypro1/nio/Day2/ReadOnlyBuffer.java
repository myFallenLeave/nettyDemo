package com.cx.nettypro1.nio.Day2;

import java.nio.ByteBuffer;

public class ReadOnlyBuffer {

    public static void main(String[] args) {

      ByteBuffer byteBuffer=ByteBuffer.allocate(64);

      for(int i=0;i<64;i++) {
          byteBuffer.put((byte) i);
      }

      byteBuffer.flip();

      //得到一个只读得Buffer
        ByteBuffer re = byteBuffer.asReadOnlyBuffer();
        System.out.println(re.getClass());

        //读取
        while (re.hasRemaining()){
            System.out.println(re.get());
        }
    }

}
