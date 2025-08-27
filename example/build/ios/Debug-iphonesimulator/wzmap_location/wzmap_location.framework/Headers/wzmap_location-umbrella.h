#ifdef __OBJC__
#import <UIKit/UIKit.h>
#else
#ifndef FOUNDATION_EXPORT
#if defined(__cplusplus)
#define FOUNDATION_EXPORT extern "C"
#else
#define FOUNDATION_EXPORT extern
#endif
#endif
#endif

#import "WzmapLocationPlugin.h"

FOUNDATION_EXPORT double wzmap_locationVersionNumber;
FOUNDATION_EXPORT const unsigned char wzmap_locationVersionString[];

