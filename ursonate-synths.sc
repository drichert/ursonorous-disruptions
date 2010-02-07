//////////// SYNTHDEFS /////////////////////////////////////////////////////////////////////
// run this first
(
/* START: BUFFER PLAYERS */

SynthDef(\buf_player, {
		arg bufnum=0, dur=1;
	var sig, rate;
	rate = BufDur.kr(bufnum) / dur;
	
	sig = PlayBuf.ar(
		numChannels: 1,
		bufnum: bufnum,
		rate: rate,
		loop: 0
	);
	Out.ar([0,1], sig);
}).send(s);


SynthDef(\buf_seg_player, {
	arg	bufnum,	// number of buffer to read from
		gate = 1,
		startPos, // frame to start playback from
		dur		// duration of playback
		;
	
	var	env = EnvGen.ar(
				Env.new([1,1,0], [dur,0.01], \linear),
				gate,
				doneAction: 2
		),
		sig = PlayBuf.ar(
				1,
				bufnum,
				1.0,
				startPos: startPos,
				loop: 1
		) * env
		;
		
	Out.ar([0,1], sig);
}).send(s);

SynthDef(\buf_rand_seg_player, {
	arg	bufnum,	// number of buffer to read from
		gate = 1,
		num_segs, // number of segments
		dur,		// duration of playback
		level = 0.5      // this controls the playback level from the input buffer
		;
	
	var	env = EnvGen.ar(
				Env.new([1,1,0], [dur,0.01], \linear),
				gate,
				doneAction: 2
		),
		seg_len = BufFrames.kr(bufnum) / num_segs,
		startPos = (num_segs + 1).rand * seg_len,
		sig = PlayBuf.ar(
				1,
				bufnum,
				1.0,
				startPos: startPos,
				loop: 1
		) * env * level
		;
		
	Out.ar([0,1], sig);
}).send(s);


/* END: BUFFER PLAYERS */


/* START: INPUT READING SYNTHS */

SynthDef(\ur_in, {
	arg 	abus_in = 1, // input signal bus (microphone)
		kbus_out, // control bus output for noise-gated control signal
		thresh	// threshold for the noise gate
		;
	var 	in_sig = SoundIn.ar(abus_in),
		ng_sig = Compander.ar(
					in_sig, in_sig,
					thresh: thresh,
					slopeBelow: 10,
					slopeAbove: 1,
					clampTime: 0.002,
					relaxTime: 0.002
		),
		ng_ctl_sig = A2K.kr(ng_sig)
		;
	Out.kr(kbus_out, ng_ctl_sig);
}).send(s);

SynthDef(\ur_write_phasor, {
	arg 	abus_out, // output bus for phasor signal
		bufnum	  // buffer number for buffer to record input to
		;
		
	Out.ar(	abus_out,
			Phasor.ar(0, BufRateScale.kr(bufnum), 0, BufFrames.kr(bufnum))
	);
}).send(s);

SynthDef(\ur_in_buf_writer, {
	arg 	audio_bus_in,  // bus of input signal to write to the buffer
		phasor_bus_in, // bus of phasor signal for indexing the buffer (audio rate)
		bufnum		 // buffer number of buffer to write to
		;
	
	BufWr.ar(		SoundIn.ar(audio_bus_in),
				bufnum: bufnum,
				phase: In.ar(phasor_bus_in),
				loop: 1
	);
}).send(s);

/* END: INPUT READING SYNTHS */
)

