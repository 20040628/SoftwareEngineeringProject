<template>
  <view class="instruction-page">
    <view class="instruction-title">How to Rent a Scooter</view>
    <view class="instruction-step" v-for="(step, index) in instructions" :key="index">
      <uni-icons :type="step.icon" size="28" color="#014d87" class="step-icon" />
      <view class="step-content">
        <view class="step-title">Step {{ index + 1 }}: {{ step.title }}</view>
        <view class="step-desc">{{ step.desc }}</view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      instructions: [
        {
          icon: 'person',
          title: 'Register and Log In',
          desc: 'You must have a valid card number to register. Forgot password? You can recover it. Agree to the terms and conditions.'
        },
        {
          icon: 'location',
          title: 'Choose Rental Location and Time',
          desc: 'Select the scooter station and your preferred rental time slot.'
        },
        {
          icon: 'compose',
          title: 'Confirm Scooter Information',
          desc: 'Check the scooter model and availability before confirming.'
        },
        {
          icon: 'wallet',
          title: 'Make Payment',
          desc: 'Pay the rental fee and a deposit (£50). If returned late or with battery below 90%, the deposit will be deducted.'
        },
        {
          icon: 'gear',
          title: 'Manage Orders',
          desc: 'You can extend your rental, return the scooter early, or on time. Late returns or wrong return locations will forfeit your deposit.'
        }
      ]
    };
  }
};
</script>

<style>
.instruction-page {
  padding: 30rpx;
  background-color: #f8f8f8;
}

.instruction-title {
  font-size: 40rpx;
  font-weight: bold;
  margin-bottom: 30rpx;
  text-align: center;
  color: #014d87;
}

.instruction-step {
  display: flex;
  flex-direction: row;
  background-color: #fff;
  border-radius: 20rpx;
  padding: 25rpx;
  margin-bottom: 25rpx;
  box-shadow: 0 6rpx 12rpx rgba(0, 0, 0, 0.05);
  align-items: flex-start;
}

.step-icon {
  margin-right: 20rpx;
  margin-top: 6rpx;
}

.step-content {
  flex: 1;
}

.step-title {
  font-size: 32rpx;
  font-weight: bold;
  margin-bottom: 10rpx;
}

.step-desc {
  font-size: 28rpx;
  color: #666;
  line-height: 1.5;
}
</style>
