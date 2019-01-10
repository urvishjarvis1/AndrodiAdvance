//
// Created by urvish on 16/5/18.
//

#ifndef ANDROIDNATIVEAPINN_SIMPLE_MODEL_H
#define ANDROIDNATIVEAPINN_SIMPLE_MODEL_H

#include <android/NeuralNetworks.h>
#include <vector>
#define FLOAT_EPISILON (1e-6)
#define TENSOR_SIZE 200
#define LOG_TAG "NNAPI_DEMO"
class SimpleModel{
public:
    explicit SimpleModel(size_t size,int protect,int fd,size_t offset);
    ~SimpleModel();
    bool CreateCompiledModel();
    bool Compute(float inputValue1, float inputValue2, float *result);

private:
    ANeuralNetworksModel *model_;
    ANeuralNetworksCompilation *compilation_;
    ANeuralNetworksMemory *memoryModel_;
    ANeuralNetworksMemory *memoryInput2_;
    ANeuralNetworksMemory *memoryOutput_;

    uint32_t dimLength_;
    uint32_t tensorSize_;
    size_t offset_;

    std::vector<float> inputTensor1_;
    int modelDataFd_;
    int inputTensor2Fd_;
    int outputTensorFd_;
};
#endif //ANDROIDNATIVEAPINN_SIMPLE_MODEL_H
