/*
 Licensed to the Apache Software Foundation (ASF) under one
 or more contributor license agreements.  See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership.  The ASF licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.
 */

#include <sys/types.h>
#include <sys/sysctl.h>
#include "TargetConditionals.h"

#import <Cordova/CDV.h>
#import "CDVScall.h"



@implementation CDVScall
    
  //  @synthesize lSUniversal=_lSUniversal;
    
- (void)pluginInitialize {
    [self setupSightCall];
}
    
- (void)call:(CDVInvokedUrlCommand*)command
    {
         NSLog(@"Init call...");
        callbackId = command.callbackId;
        callUrl = [command argumentAtIndex:0];
        NSLog(@"Calling ... %@ ",callUrl);

        
        [lSUniversal startWithString: callUrl];
    }
    
- (void)setupSightCall
    {
        NSLog(@"Setting up sight call...");
        lSUniversal = [[LSUniversal alloc] init];
        id<LSUniversalDelegate> scallDelegatePointer = self;
        lSUniversal.delegate = scallDelegatePointer;
        NSLog(@"Sight call initialized");
    }
- (void)connectionEvent:(lsConnectionStatus_t)status
{
    switch (status) {
        case lsConnectionStatus_idle:
            NSLog(@"lsConnectionStatus_idle...");
            break;
        case lsConnectionStatus_connecting:
            NSLog(@"lsConnectionStatus_connecting...");
            break;
        case lsConnectionStatus_active:
            NSLog(@"lsConnectionStatus_active...");
            break;
            
        case lsConnectionStatus_calling:
            NSLog(@"lsConnectionStatus_calling...");
            break;
        case lsConnectionStatus_callActive:
        {     NSLog(@"lsConnectionStatus_callActive...");
            dispatch_async(dispatch_get_main_queue(), ^{
                [self.viewController presentViewController:lSUniversal.callViewController animated:YES completion:nil];
            });
        }   break;
        case lsConnectionStatus_disconnecting: break;
    }
}

- (void)connectionError:(lsConnectionError_t)error
{
    NSLog(@"connectionError...");
}

- (void)callReport:(lsCallReport_s)callEnd
{
    NSLog(@"callReport...");
}

    @end
