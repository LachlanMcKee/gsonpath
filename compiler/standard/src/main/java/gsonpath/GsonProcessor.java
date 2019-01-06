package gsonpath;

import com.google.auto.service.AutoService;

import javax.annotation.processing.Processor;

/**
 * Note: Autoservice does not seem to work when using a kotlin class, therefore we
 * expose this empty java class to appease the library.
 */
@AutoService(Processor.class)
public class GsonProcessor extends GsonProcessorImpl {
}