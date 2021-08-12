//
// Created by bli on 2021/8/12.
//

#ifndef INSIGHTFACE_YUVCONVERTER_H
#define INSIGHTFACE_YUVCONVERTER_H
#include <algorithm>
class Yuv2Rgb{
public:
    // convert yuv420sp(nv21) to rgb, the fast approximate version
    static void yuv420sp2rgb(const unsigned char* yuv420sp, int w, int h, unsigned char* rgb);
// convert yuv420sp(nv12) to rgb, the fast approximate version
    static void yuv420sp2rgb_nv12(const unsigned char* yuv420sp, int w, int h, unsigned char* rgb);
// convert yuv420sp(nv21) to rgb with half resize, the faster approximate version
    static void yuv420sp2rgb_half(const unsigned char* yuv420sp, int w, int h, unsigned char* rgb);
};

#endif //INSIGHTFACE_YUVCONVERTER_H
