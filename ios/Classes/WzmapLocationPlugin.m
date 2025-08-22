#import "WzmapLocationPlugin.h"
#if __has_include(<wzmap_location/wzmap_location-Swift.h>)
#import <wzmap_location/wzmap_location-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "wzmap_location-Swift.h"
#endif

@implementation WzmapLocationPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftWzmapLocationPlugin registerWithRegistrar:registrar];
}
@end
