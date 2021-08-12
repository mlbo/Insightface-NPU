//
// Created by bli on 2021/8/12.
//

#ifndef INSIGHTFACE_IMAGEROTATE_H
#define INSIGHTFACE_IMAGEROTATE_H
class Rotate{
public:

    static void kanna_rotate_c1(const unsigned char *src, int srcw, int srch, unsigned char *dst, int w, int h,
                         int type);

    static void kanna_rotate_c2(const unsigned char *src, int srcw, int srch, unsigned char *dst, int w, int h,
                         int type);

    static void kanna_rotate_c3(const unsigned char *src, int srcw, int srch, unsigned char *dst, int w, int h,
                         int type);

    static void kanna_rotate_c4(const unsigned char *src, int srcw, int srch, unsigned char *dst, int w, int h,
                         int type);

// image pixel kanna rotate with stride(bytes-per-row) parameter
    static void
    kanna_rotate_c1(const unsigned char *src, int srcw, int srch, int srcstride, unsigned char *dst,
                    int w, int h, int stride, int type);

    static void
    kanna_rotate_c2(const unsigned char *src, int srcw, int srch, int srcstride, unsigned char *dst,
                    int w, int h, int stride, int type);

    static void
    kanna_rotate_c3(const unsigned char *src, int srcw, int srch, int srcstride, unsigned char *dst,
                    int w, int h, int stride, int type);

    static void
    kanna_rotate_c4(const unsigned char *src, int srcw, int srch, int srcstride, unsigned char *dst,
                    int w, int h, int stride, int type);

// image pixel kanna rotate, convenient wrapper for yuv420sp(nv21/nv12)
    static void kanna_rotate_yuv420sp(const unsigned char *src, int srcw, int srch, unsigned char *dst, int w,
                               int h, int type);

};

#endif //INSIGHTFACE_IMAGEROTATE_H
